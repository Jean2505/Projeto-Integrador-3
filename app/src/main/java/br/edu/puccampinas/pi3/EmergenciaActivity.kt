package br.edu.puccampinas.pi3

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class EmergenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciaBinding
    private val user = Firebase.auth.currentUser
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = Firebase.storage
        val storageRef1 = storage.getReferenceFromUrl(intent.getStringExtra("Foto1").toString())
        val localFile1 = File.createTempFile("images","jpg")


        val storageRef2 = storage.getReferenceFromUrl(intent.getStringExtra("Foto2").toString())
        val localFile2 = File.createTempFile("images1","jpg")

        val storageRef3 = storage.getReferenceFromUrl(intent.getStringExtra("Foto3").toString())
        val localFile3 = File.createTempFile("images2","jpg")

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



        binding.btnAceitar.setOnClickListener {
            //manda aceite pro banco
            db.collection("dentistas").whereEqualTo("uid", user!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val doc = hashMapOf(
                            "profissional" to user.uid,
                            "status" to "aceita"
                        )
                        db.collection("aceites").add(doc)
                    }
                }
        }

        binding.btnRecusar.setOnClickListener {
            //manda recusa pro banco
            db.collection("dentistas").whereEqualTo("uid", user!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val doc = hashMapOf(
                            "profissional" to user.uid,
                            "status" to "recusada"
                        )
                        db.collection("aceites").add(doc)
                    }
                }
        }
    }
}