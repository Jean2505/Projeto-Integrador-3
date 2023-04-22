package br.edu.puccampinas.pi3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.puccampinas.pi3.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
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
                binding.etSenha.text.toString())

                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val iLogado = Intent(this, EmergenciaActivity::class.java)
                        if (user != null) {
                            iLogado.putExtra("email", user.email)
                            this.startActivity(iLogado)
                        }
                    } else {
                        Toast.makeText(
                            baseContext, "Email ou senha incorreta, tente novamente!",
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