package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import com.google.android.material.button.MaterialButton

class AvaliacaoActivity : AppCompatActivity() {

    private lateinit var btnChamadas : MaterialButton
    private lateinit var tvComent : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

        val dataSetDeAvaliacoes = avaliacoesList()
        val avalAdapter = AvaliacoesAdapter(dataSetDeAvaliacoes)
        val recyclerView: RecyclerView = findViewById(R.id.rvAvaliacoes)
        recyclerView.adapter = avalAdapter

        btnChamadas = findViewById(R.id.btnChamadas)

        btnChamadas.setOnClickListener{
            val intentChamadas = Intent(this,EmergenciasActivity::class.java)
            startActivity(intentChamadas)
        }

    }
}