package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    private lateinit var btnVoltarL : AppCompatButton
    private lateinit var btnProximo : Button
    private lateinit var etNome : EditText
    private lateinit var etTelef : EditText
    private lateinit var etEmail : EditText
    private lateinit var etSenha : EditText
    private lateinit var etEnd1 : EditText
    private lateinit var etEnd2 : EditText
    private lateinit var etEnd3 : EditText
    private lateinit var etCurriculo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        btnVoltarL = findViewById(R.id.btnVoltarL)
        btnProximo = findViewById(R.id.btnProximo)
        etNome = findViewById(R.id.etNome)
        etTelef = findViewById(R.id.etTelef)
        etEmail = findViewById(R.id.etEmail)
        etSenha = findViewById(R.id.etSenha)
        etEnd1 = findViewById(R.id.etEnd1)
        etEnd2 = findViewById(R.id.etEnd2)
        etEnd3 = findViewById(R.id.etEnd3)
        etCurriculo = findViewById(R.id.etCurriculo)

        btnProximo.setOnClickListener(this)
        btnVoltarL.setOnClickListener(this)


    }

    override fun onClick(v: View?){

        if(v!!.id == R.id.btnProximo){
        val nome = etNome.text.toString()
        val telefone = etTelef.text.toString()
        val email = etEmail.text.toString()
        val senha = etSenha.text.toString()
        val enderecoUm = etEnd1.text.toString()
        val enderecoDois = etEnd2.text.toString()
        val enderecoTres = etEnd3.text.toString()
        val curriculo = etCurriculo.text.toString()

        Firebase.auth.signOut()

        val intentProximo = Intent(this,CurriculoActivity::class.java)

        intentProximo.putExtra("INome", nome)
        intentProximo.putExtra("ITelefone", telefone)
        intentProximo.putExtra("IEmail", email)
        intentProximo.putExtra("ISenha", senha)
        intentProximo.putExtra("IEnderecoUm", enderecoUm)
        intentProximo.putExtra("IEnderecoDois", enderecoDois)
        intentProximo.putExtra("IEnderecoTres", enderecoTres)
        intentProximo.putExtra("ICurriculo", curriculo)

        this.startActivity(intentProximo)
        }

        if(v!!.id == R.id.btnVoltarL){
                val voltarTela = Intent(this, LoginActivity::class.java)
                this.startActivity(voltarTela)
        }
    }

}