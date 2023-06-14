package br.edu.puccampinas.pi3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import java.io.File


class EmergenciaActivity : AppCompatActivity() {

    private val phoneNumber = "998760722"


    private lateinit var binding: ActivityEmergenciaBinding
    private val user = Firebase.auth.currentUser
    private var db = FirebaseFirestore.getInstance()
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        functions = Firebase.functions("southamerica-east1")

        binding.btnLigar.isClickable = false

        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverAceite")

        val rv = RecieverAceite()
        registerReceiver(rv,Receiver)



        val foto1 = "gs://prijinttres.appspot.com/${intent.getStringExtra("Foto1").toString()}"
        val storage = Firebase.storage
        val storageRef1 = storage.getReferenceFromUrl(foto1)
        val localFile1 = File.createTempFile("images","jpg")

        val foto2 = "gs://prijinttres.appspot.com/${intent.getStringExtra("Foto2").toString()}"
        val storageRef2 = storage.getReferenceFromUrl(foto2)
        val localFile2 = File.createTempFile("images2","jpg")

        val foto3 = "gs://prijinttres.appspot.com/${intent.getStringExtra("Foto3").toString()}"
        val storageRef3 = storage.getReferenceFromUrl(foto3)
        val localFile3 = File.createTempFile("images3","jpg")

        storageRef1.getFile(localFile1).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile1.absolutePath)
            Picasso.with(this).load("file:" + localFile1.absolutePath).fit().centerInside().into(binding.ivFoto1)

        }.addOnFailureListener {

            Toast.makeText(this, "Erro ao baixar imagem", Toast.LENGTH_SHORT).show()
        }

        storageRef2.getFile(localFile2).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile2.absolutePath)
            Picasso.with(this).load("file:" + localFile2.absolutePath).fit().centerInside().into(binding.ivFoto2)

        }.addOnFailureListener {

            Toast.makeText(this, "Erro ao baixar imagem", Toast.LENGTH_SHORT).show()
        }

        storageRef3.getFile(localFile3).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile3.absolutePath)
            Picasso.with(this).load("file:" + localFile3.absolutePath).fit().centerInside().into(binding.ivFoto3)

        }.addOnFailureListener {

            Toast.makeText(this, "Erro ao baixar imagem", Toast.LENGTH_SHORT).show()
        }

        binding.tvNome.text = intent.getStringExtra("nome")
        binding.tvTelefone.text = intent.getStringExtra("telefone")
        binding.tvDataHora.text = intent.getStringExtra("dataHora")


        binding.btnVoltar.setOnClickListener {
            this.finish()
        }


        binding.btnAceitar.setOnClickListener {
            //manda aceite pro banco
            db.collection("dentistas").whereEqualTo("uid", user!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("status", false)
                        val doc = hashMapOf(
                            "profissional" to user.uid,
                            "status" to "aceita",
                            "emergencia" to intent.getStringExtra("emergencia")
                        )
                        db.collection("aceites").add(doc).addOnSuccessListener {
                            Toast.makeText(this, "Emergência aceita!", Toast.LENGTH_SHORT).show()
                            binding.btnAceitar.isClickable = false
                            binding.btnRecusar.isClickable = false

                        }
                    }
                }
        }

        binding.btnLigar.setOnClickListener{
            enviarLigacao()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                            Toast.makeText(this, "Erro ao fazer ligação, tente novamente!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        val iAndamento = Intent(this, AndamentoActivity::class.java)
                        iAndamento.putExtra("emergencia",intent.getStringExtra("emergencia"))
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(iAndamento)
                            realizarChamada()
                        } else {
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                        }
                    }
                })
        }

        binding.btnRecusar.setOnClickListener {
            //manda recusa pro banco
            db.collection("dentistas").whereEqualTo("uid", user!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val doc = hashMapOf(
                            "profissional" to user.uid,
                            "status" to "recusada",
                            "emergencia" to intent.getStringExtra("emergencia")
                        )
                        db.collection("aceites").add(doc).addOnSuccessListener {
                            Toast.makeText(this, "Emergência recusada!", Toast.LENGTH_SHORT).show()
                            binding.btnAceitar.isClickable = false
                            binding.btnRecusar.isClickable = false
                        }
                    }
                }
        }
    }

    private fun realizarChamada() {
        val iLigar = Intent(Intent.ACTION_CALL)
        iLigar.data = Uri.parse("tel:${intent.getStringExtra("telefone")}")

        if (iLigar.resolveActivity(packageManager) != null) {
            startActivity(iLigar)
        } else {
            Toast.makeText(this, "Não é possível realizar chamadas neste dispositivo.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarLigacao(): Task<String> {

        val data = hashMapOf(
            "emerg" to intent.getStringExtra("emergencia")
        )
        return functions
            .getHttpsCallable("dentistaLigou")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }

    inner class RecieverAceite : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if(intent.getStringExtra("status") == "aceita") {
                Toast.makeText(context, "Você foi escolhido! Agora, entre em contato com o " +
                        "socorrista", Toast.LENGTH_LONG).show()
                binding.tvTelefone.inputType = 3;
                binding.btnLigar.isClickable = true;
            }
            else if (intent.getStringExtra("status") == "rejeitada") {
                Toast.makeText(context, "Emergência atribuida a outro profissional", Toast.LENGTH_LONG).show()
                val irecusa = Intent(context,EmergenciasActivity::class.java)
                startActivity(irecusa)
            }
        }
    }
}