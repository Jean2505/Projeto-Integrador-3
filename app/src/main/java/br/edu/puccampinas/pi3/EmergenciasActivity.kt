package br.edu.puccampinas.pi3

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciasBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class EmergenciasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencias)

        binding = ActivityEmergenciasBinding.inflate(layoutInflater)
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