package br.edu.puccampinas.pi3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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



        var teste: List<Emergencia> = emptyList()
        val dataSetDeTarefas = emergenciaList()
        val lAU = Emergencia(nome = "TESTEEEEEASDASDSAD",
            telefone = "11111-1111",
            foto1 = "cywhrvbuyewb",
            foto2 = "cbhewvcbuye",
            foto3 = "cbhuredbcuj",
            status = "Nova",
            dataHora = "22/04/2023 15:27")
        val listaTeste =  teste.plus(lAU)
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

    inner class ReceiverEmergencia : BroadcastReceiver() {
        var lista: MutableList<Emergencia> = emptyList<Emergencia>().toMutableList()

        override fun onReceive(context: Context, intent: Intent) {
            // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
            Toast.makeText(context, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show()

            val Emerg = Emergencia(nome = intent.getStringExtra("nome")!!,
                telefone = intent.getStringExtra("telefone")!!,
                foto1 = intent.getStringExtra("Foto1")!!,
                foto2 = intent.getStringExtra("Foto2")!!,
                foto3 = intent.getStringExtra("Foto3")!!,
                status = "Nova",
                dataHora = "69/04/2023 15:27")
            lista.add(Emerg)

            val EmerAdapter = EmergenciasAdapter(lista)
            val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
            recyclerView.adapter = EmerAdapter
        }
    }
}