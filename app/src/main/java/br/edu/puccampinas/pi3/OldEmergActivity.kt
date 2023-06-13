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
import br.edu.puccampinas.pi3.databinding.ActivityOldemergBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class OldEmergActivity : AppCompatActivity() {

    private val phoneNumber = "998760722"
    //private lateinit var phoneNumber: String

    private lateinit var binding: ActivityOldemergBinding
    private val user = Firebase.auth.currentUser
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oldemerg)

        binding = ActivityOldemergBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "SOCORRO PELO AMOR DE DEUS", Toast.LENGTH_LONG).show()
        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverAceite")

        val rv = RecieverAceite()
        registerReceiver(rv,Receiver)

        //phoneNumber = intent.getStringExtra("telefone").toString()

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
            // Local temp file has been created
            val bitmap = BitmapFactory.decodeFile(localFile1.absolutePath)
            binding.ivFoto1.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this, "deu errado irmão", Toast.LENGTH_SHORT).show()
        }

        storageRef2.getFile(localFile2).addOnSuccessListener {
            // Local temp file has been created
            val bitmap = BitmapFactory.decodeFile(localFile2.absolutePath)
            binding.ivFoto2.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this, "deu errado irmão", Toast.LENGTH_SHORT).show()
        }

        storageRef3.getFile(localFile3).addOnSuccessListener {
            // Local temp file has been created
            val bitmap = BitmapFactory.decodeFile(localFile3.absolutePath)
            binding.ivFoto3.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this, "deu errado irmão", Toast.LENGTH_SHORT).show()
        }

        binding.tvNome.text = intent.getStringExtra("nome")
        binding.tvTelefone.text = intent.getStringExtra("telefone")
        binding.tvDataHora.text = intent.getStringExtra("dataHora")


        binding.btnVoltar.setOnClickListener {
            this.finish()
            //val iVoltar = Intent(this,EmergenciasActivity::class.java)
            //this.startActivity(iVoltar)
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

    inner class RecieverAceite : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
            //Toast.makeText(context, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show()

            if(intent.getStringExtra("status") == "aceita") {
                Toast.makeText(context, "Você foi escolhido! Agora, entre em contato com o " +
                        "socorrista", Toast.LENGTH_LONG).show()
                binding.tvTelefone.inputType = 3;
            }
            else if (intent.getStringExtra("status") == "rejeitada") {
                Toast.makeText(context, "Emergência atribuida a outro profissional", Toast.LENGTH_LONG).show()
                val irecusa = Intent(context,EmergenciasActivity::class.java)
                startActivity(irecusa)
            }
        }
    }
}