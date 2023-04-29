package br.edu.puccampinas.pi3

import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding

class EmergenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, intent.getStringExtra("nome"), Toast.LENGTH_SHORT).show()
        binding.tvNome.text = intent.getStringExtra("nome")
        binding.tvTelefone.text = intent.getStringExtra("telefone")
        //binding.tvFoto1.sr = intent.getStringExtra("foto1")
        //binding.tvFoto2.src = intent.getStringExtra("foto2")
        //binding.tvFoto3.text = intent.getStringExtra("foto3")
        //binding.ivFoto1.setImageResource(R.drawable.bebe2)

        binding.tvDataHora.text = intent.getStringExtra("dataHora")
    }
}