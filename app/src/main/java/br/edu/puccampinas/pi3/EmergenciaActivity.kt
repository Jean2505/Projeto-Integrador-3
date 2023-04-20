package br.edu.puccampinas.pi3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class EmergenciaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        val dataSetDeTarefas = emergenciaList()
        val EmerAdapter = EmergenciasAdapter(dataSetDeTarefas)
        val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
        recyclerView.adapter = EmerAdapter
    }
}