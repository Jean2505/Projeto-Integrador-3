package br.edu.puccampinas.pi3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciasBinding
import br.edu.puccampinas.pi3.databinding.ActivityHistoricoBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage


class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding
    private var db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser
    var lista: MutableList<Emergencia> = emptyList<Emergencia>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergencias)

        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverEmergencia")

        var teste: List<Emergencia> = emptyList()
        val dataSetDeTarefas = emergenciaList()

        /*val EmerAdapter = HistoricoAdapter(dataSetDeTarefas)
        val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
        recyclerView.adapter = EmerAdapter*/

        db.collection("aceites").whereEqualTo("profissional", user!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                Toast.makeText(this, "passou um aviao", Toast.LENGTH_SHORT).show()

                for(document in documents) {
                    val stat = document["status"]
                    db.collection("emergencias").document(document["emergencia"].toString())
                        .get()
                        .addOnSuccessListener { document2 ->
                            val emerg = Emergencia(
                                nome = document2["nome"].toString(),
                                telefone = document2["telefone"].toString(),
                                foto1 = document2["Foto1"].toString(),
                                foto2 = document2["Foto2"].toString(),
                                foto3 = document2["Foto3"].toString(),
                                status = stat.toString(),
                                dataHora = document2["dataHora"].toString(),
                                emergencia = "document2id"
                            )
                            lista.add(emerg)
                            val EmerAdapter = HistoricoAdapter(lista.asReversed())
                            val recyclerView: RecyclerView = findViewById(R.id.rvEmergencias)
                            recyclerView.adapter = EmerAdapter
                        }
                }
            }


        binding.btnAvaliacoes.setOnClickListener {
            val intentAvaliacoes = Intent(this,AvaliacaoActivity::class.java)
            startActivity(intentAvaliacoes)
        }

        binding.btnPerfil.setOnClickListener{
            intent.getStringExtra("email")

            val iPerfil = Intent(this, PerfilActivity::class.java)
            iPerfil.putExtra("email", intent.getStringExtra("email"))
            this.startActivity(iPerfil)
        }
    }
}