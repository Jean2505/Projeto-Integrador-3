package br.edu.puccampinas.pi3

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        binding.btnDeslogar.setOnClickListener {
            Firebase.auth.signOut()
            val iLogin = Intent(this, LoginActivity::class.java)
            startActivity(iLogin)
        }

        binding.scSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            attStatus(isChecked)
            if(isChecked)
            {
                binding.tvStatus.text = "Disponível"
            }else{
                binding.tvStatus.text = "Ocupado"
            }
        }

        binding.btnVoltar.setOnClickListener{
            this.finish()
            //val intent = Intent(this, EmergenciasActivity::class.java)
            //this.startActivity(intent)
        }

        binding.btnEdit.setOnClickListener{

            val newbackground = R.drawable.boxedit
            val corIcone = ContextCompat.getColor(this, R.color.azulescuro)

            binding.etCv.setBackgroundResource(newbackground)
            binding.etNome.setBackgroundResource(newbackground)
            binding.etEmail.setBackgroundResource(newbackground)
            binding.etTelef.setBackgroundResource(newbackground)
            binding.etSenha.setBackgroundResource(newbackground)
            binding.etEnd1.setBackgroundResource(newbackground)
            binding.etEnd2.setBackgroundResource(newbackground)
            binding.etEnd3.setBackgroundResource(newbackground)

            binding.etCv.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etNome.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etTelef.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etEmail.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etSenha.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etEnd1.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etEnd2.compoundDrawableTintList = ColorStateList.valueOf(corIcone)
            binding.etEnd3.compoundDrawableTintList = ColorStateList.valueOf(corIcone)


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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etEmail.setBackgroundResource(newbackground)

            binding.etEmail.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

            if(intent.getStringExtra("email") != null){
                //email = intent.getStringExtra("email")!!
                email = user!!.email.toString()
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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etSenha.setBackgroundResource(newbackground)

            binding.etSenha.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etNome.setBackgroundResource(newbackground)
            binding.etNome.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etTelef.setBackgroundResource(newbackground)
            binding.etTelef.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etEnd1.setBackgroundResource(newbackground)
            binding.etEnd1.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etEnd2.setBackgroundResource(newbackground)
            binding.etEnd2.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etEnd3.setBackgroundResource(newbackground)
            binding.etEnd3.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

            val newbackground = R.drawable.textbox
            val corIcone = ContextCompat.getColor(this, R.color.azulclaro)

            binding.etCv.setBackgroundResource(newbackground)
            binding.etCv.compoundDrawableTintList = ColorStateList.valueOf(corIcone)

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

    private fun attStatus(status: Boolean) {
        db.collection("dentistas").whereEqualTo("email", user!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    db.collection("dentistas").document(document.id)
                        .update("status", status)
                }
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

                                //Toast.makeText(this, documento.getString("status"), Toast.LENGTH_SHORT).show()

                                if(documento.getBoolean("status") == true) {
                                    binding.scSwitch.isChecked = true
                                    binding.tvStatus.text = "Disponível"
                                }
                                else if (documento.getBoolean("status") == false) {
                                    binding.scSwitch.isChecked = false
                                    binding.tvStatus.text = "Ocupado"
                                }
                            }
                        }
                }
            }
    }


}