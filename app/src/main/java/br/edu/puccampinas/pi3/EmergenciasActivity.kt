package br.edu.puccampinas.pi3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciasBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage


class EmergenciasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencias)

        binding = ActivityEmergenciasBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverEmergencia")

        val rv = ReceiverEmergencia()
        registerReceiver(rv,Receiver)





        val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
        if(recyclerView.childCount.toString() == "0"){
            binding.tvTeste.text = "Você não tem novas emergências!"
        }



        binding.btnAvaliacoes.setOnClickListener {
            val iAvaliacoes = Intent(this,AvaliacaoActivity::class.java)
            iAvaliacoes.putExtra("finish", "sim")
            startActivity(iAvaliacoes)
        }

        binding.btnPerfil.setOnClickListener{
            intent.getStringExtra("email")

            val iPerfil = Intent(this, PerfilActivity::class.java)
            iPerfil.putExtra("email", intent.getStringExtra("email"))
            this.startActivity(iPerfil)
        }

        binding.btnHistorico.setOnClickListener {
            val iHistorico = Intent(this, HistoricoActivity::class.java)
            iHistorico.putExtra("finish", "sim")
            this.startActivity(iHistorico)
        }
    }

    inner class ReceiverEmergencia : BroadcastReceiver() {
        var lista: MutableList<Emergencia> = emptyList<Emergencia>().toMutableList()
        var i = 0

        override fun onReceive(context: Context, intent: Intent) {

            binding.tvTeste.text = ""

            val Emerg = Emergencia(nome = intent.getStringExtra("nome")!!,
                telefone = intent.getStringExtra("telefone")!!,
                foto1 = intent.getStringExtra("Foto1")!!,
                foto2 = intent.getStringExtra("Foto2")!!,
                foto3 = intent.getStringExtra("Foto3")!!,
                status = "Nova",
                dataHora = intent.getStringExtra("dataHora")!!,
                emergencia = intent.getStringExtra("emergencia")!!)
            lista.add(Emerg)

            i += 1;
            val EmerAdapter = EmergenciasAdapter(lista.asReversed())
            val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
            recyclerView.adapter = EmerAdapter
        }
    }
}