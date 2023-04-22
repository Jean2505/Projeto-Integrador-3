package br.edu.puccampinas.pi3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciasBinding

class EmergenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencia)

        binding = ActivityEmergenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNome.text = intent.getStringExtra("nome")
        binding.tvTelefone.text = intent.getStringExtra("telefone")
        binding.tvFoto1.text = intent.getStringExtra("foto1")
        binding.tvFoto2.text = intent.getStringExtra("foto2")
        binding.tvFoto3.text = intent.getStringExtra("foto3")
        binding.tvStatus.text = intent.getStringExtra("status")
        binding.tvDataHora.text = intent.getStringExtra("dataHora")
    }
}