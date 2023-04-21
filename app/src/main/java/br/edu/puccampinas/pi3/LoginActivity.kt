package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogar.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etSenha.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed." + task.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        binding.btnCadastrar.setOnClickListener{
            val intentCadastra = Intent(this,MainActivity::class.java)

            this.startActivity(intentCadastra)
        }
    }
}