package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityPerfilBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PerfilActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()
    private lateinit var email: String
    private lateinit var binding: ActivityPerfilBinding
    private val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val user = Firebase.auth.currentUser

        binding.btnEdit.setOnClickListener{
            binding.etEmail.isEnabled = true
            binding.btnConcEmail.isEnabled = true
            binding.etSenha.text = null
            binding.etSenha.isEnabled = true
            binding.btnConcSenha.isEnabled = true
            binding.etNome.isEnabled = true
            binding.btnConcNome.isEnabled = true
            binding.etTelef.isEnabled = true
            binding.btnConcTelef.isEnabled = true
            binding.etEnd1.isEnabled = true
            binding.btnConcEnd1.isEnabled = true
            binding.etEnd2.isEnabled = true
            binding.btnConcEnd2.isEnabled = true
            binding.etEnd3.isEnabled = true
            binding.btnConcEnd3.isEnabled = true
            binding.etCv.isEnabled = true
            binding.btnConcCv.isEnabled = true
        }

        binding.btnConcEmail.setOnClickListener{

            if(intent.getStringExtra("email") != null){
                email = intent.getStringExtra("email")!!
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
            }

            db.collection("dentistas").whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("email", binding.etEmail.text.toString())
                    }
                }

            user!!.updateEmail(binding.etEmail.text.toString())
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email Atualizado", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Para alterar seu email, faça o login novamente!", Toast.LENGTH_LONG).show()
                        Firebase.auth.signOut()
                        val iRelog = Intent(this, LoginActivity::class.java)
                        this.startActivity(iRelog)
                    }
                }
        }

        binding.btnConcSenha.setOnClickListener{
            user!!.updatePassword(binding.etSenha.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Senha Atualizada", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Para alterar sua senha, faça o login novamente!", Toast.LENGTH_LONG).show()
                        Firebase.auth.signOut()
                        val iRelog = Intent(this, LoginActivity::class.java)
                        this.startActivity(iRelog)
                    }
                }
        }

        binding.btnConcNome.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("name", binding.etNome.text.toString())
                    }
                }
        }

        binding.btnConcTelef.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("telefone", binding.etTelef.text.toString())
                    }
                }
        }

        binding.btnConcEnd1.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("end1", binding.etEnd1.text.toString())
                    }
                }
        }

        binding.btnConcEnd2.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("end2", binding.etEnd2.text.toString())
                    }
                }
        }

        binding.btnConcEnd3.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("end3", binding.etEnd3.text.toString())
                    }
                }
        }

        binding.btnConcCv.setOnClickListener{
            db.collection("dentistas").whereEqualTo("email", user!!.email)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        db.collection("dentistas").document(document.id)
                            .update("cv", binding.etCv.text.toString())
                    }
                }
        }




        binding.etCv.setOnClickListener{
            binding.etCv.hint = null
        }
    }

    public override fun onStart() {
        super.onStart()
        binding.etEmail.hint = user!!.email
        db.collection("dentistas").whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    db.collection("dentistas").document(document.id)
                        .addSnapshotListener{documento, error ->
                            if(documento != null){
                                binding.etNome.hint = documento.getString("name")
                                binding.etTelef.hint = documento.getString("telefone")
                                binding.etEnd1.hint = documento.getString("end1")
                                binding.etEnd2.hint = documento.getString("end2")
                                binding.etEnd3.hint = documento.getString("end3")
                                binding.etCv.hint = documento.getString("cv")
                            }
                        }
                }
            }
    }



}