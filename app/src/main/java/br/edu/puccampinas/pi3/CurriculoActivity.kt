package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.GsonBuilder

class CurriculoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnVoltar : Button
    private lateinit var btnCadastrar : Button
    private lateinit var etCurriculo : EditText

    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriculo)

        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnVoltar = findViewById(R.id.btnVoltar)
        etCurriculo = findViewById(R.id.etCurriculo)

        val nome = intent.getStringExtra("INome")
        val email = intent.getStringExtra("IEmail")
        val telefone = intent.getStringExtra("ITelefone")
        val enderecoUm = intent.getStringExtra("IEnderecoUm")
        val enderecoDois = intent.getStringExtra("IEnderecoDois")
        val enderecoTres = intent.getStringExtra("IEnderecoTres")

        btnCadastrar.setOnClickListener(this)
        btnVoltar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Toast.makeText(this, "sas", Toast.LENGTH_LONG).show()
        if(v!!.id == R.id.btnCadastrar){
            val curriculo = etCurriculo.text.toString()
            Toast.makeText(this, "sas", Toast.LENGTH_LONG).show()
        }
        else if(v!!.id == R.id.btnVoltar){
            val intentVoltar = Intent(this,MainActivity::class.java)
            this.startActivity(intentVoltar)
        }
    }
}