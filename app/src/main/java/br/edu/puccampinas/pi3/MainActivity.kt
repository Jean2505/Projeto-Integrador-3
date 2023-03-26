package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnProximo : Button
    private lateinit var etNome : EditText
    private lateinit var etTelef : EditText
    private lateinit var etEmail : EditText
    private lateinit var etEnd1 : EditText
    private lateinit var etEnd2 : EditText
    private lateinit var etEnd3 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnProximo = findViewById(R.id.btnProximo)
        etNome = findViewById(R.id.etNome)
        etTelef = findViewById(R.id.etTelef)
        etEmail = findViewById(R.id.etEmail)
        etEnd1 = findViewById(R.id.etEnd1)
        etEnd2 = findViewById(R.id.etEnd2)
        etEnd3 = findViewById(R.id.etEnd3)

        btnProximo.setOnClickListener(this)

    }

    override fun onClick(v: View?){
        Toast.makeText(this, "sas", Toast.LENGTH_LONG).show()
        val nome = etNome.text.toString()
        val telefone = etTelef.text.toString()
        val email = etEmail.text.toString()
        val enderecoUm = etEnd1.text.toString()
        val enderecoDois = etEnd2.text.toString()
        val enderecoTres = etEnd3.text.toString()

        val intentProximo = Intent(this,CurriculoActivity::class.java)

        intentProximo.putExtra("INome", nome)
        intentProximo.putExtra("ITelefone", telefone)
        intentProximo.putExtra("IEmail", email)
        intentProximo.putExtra("IEnderecoUm", enderecoUm)
        intentProximo.putExtra("IEnderecoDois", enderecoDois)
        intentProximo.putExtra("IEnderecoTres", enderecoTres)

        this.startActivity(intentProximo)

    }

}