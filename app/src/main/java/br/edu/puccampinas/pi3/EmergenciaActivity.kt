package br.edu.puccampinas.pi3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import br.edu.puccampinas.pi3.databinding.ActivityPerfilBinding

class EmergenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSetDeTarefas = emergenciaList()
        val EmerAdapter = EmergenciasAdapter(dataSetDeTarefas)
        val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
        recyclerView.adapter = EmerAdapter

        binding.btnPerfil.setOnClickListener{
            intent.getStringExtra("email")

            val iPerfil = Intent(this, PerfilActivity::class.java)
            iPerfil.putExtra("email", intent.getStringExtra("email"))
            this.startActivity(iPerfil)
        }
    }
}