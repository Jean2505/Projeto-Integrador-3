package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoadingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        auth = Firebase.auth

    }

    public override fun onStart() {
        super.onStart()
        Firebase.auth.signOut()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "qualquer coisa que eu quiser", Toast.LENGTH_SHORT).show()

            val iLogado = Intent(this,EmergenciaActivity::class.java)

            iLogado.putExtra("email", currentUser.email)
            this.startActivity(iLogado)
        }
        else{
            if (currentUser != null) {
                Toast.makeText(this,currentUser.email.toString(), Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "ta muito nulo", Toast.LENGTH_SHORT).show()
            }
            val intentLogin = Intent(this,LoginActivity::class.java)

            this.startActivity(intentLogin)
        }
    }
}