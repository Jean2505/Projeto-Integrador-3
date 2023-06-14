package br.edu.puccampinas.pi3

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityAndamentoBinding
import br.edu.puccampinas.pi3.databinding.ActivityAvaliacaoBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.util.ArrayList


class AvaliacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvaliacaoBinding
    private lateinit var btnChamadas : MaterialButton
    private lateinit var tvComent : TextView
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val user = Firebase.auth.currentUser
    private var db = FirebaseFirestore.getInstance()
    private var lista: MutableList<Avaliacoes> = emptyList<Avaliacoes>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliacao)

        binding = ActivityAvaliacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        btnChamadas = findViewById(R.id.btnChamadas)

        functions = Firebase.functions("southamerica-east1")

        getMedia()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                        Toast.makeText(this, "Erro ao obter media!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                    val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)


                    println(genericResp.message)
                    val media = String.format("%.1f", genericResp.message!!.toDouble())
                    if (!media.toDouble().isNaN()){
                        println("entrou aqui ó")
                        binding.tvMedia.text = "sua média é : ${media}"
                    } else{
                        binding.tvMedia.text = "Você não tem avaliações"
                    }

                }
            })

        db.collection("avaliacoes").whereEqualTo("uidDentista", user!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    val avaliacao = Avaliacoes(nome = document["nome"].toString(),
                                                comentario = "\"${document["coment"].toString()}\"",
                                                estrela = document["aval"] as Long)
                    lista.add(avaliacao)

                    val avalAdapter = AvaliacoesAdapter(lista.asReversed())
                    val recyclerView: RecyclerView = findViewById(R.id.rvAvaliacoes)
                    recyclerView.adapter = avalAdapter
                }
            }

        btnChamadas.setOnClickListener{
            if(intent.getStringExtra("finish") == "sim"){
                this.finish()
            } else {
                val iEmergencias = Intent(this, EmergenciasActivity::class.java)
                startActivity(iEmergencias)
            }
        }

        binding.btnPerfil.setOnClickListener {
            val iPerfil = Intent(this, PerfilActivity::class.java)
            startActivity(iPerfil)
        }

        binding.btnHistorico.setOnClickListener {
            val iHistorico = Intent(this, HistoricoActivity::class.java)
            startActivity(iHistorico)
        }
    }

    private fun getMedia(): Task<String> {

        val data = hashMapOf(
            "uid" to user!!.uid
        )
        return functions
            .getHttpsCallable("getMedia")
            .call(data)
            .continueWith { task ->
                println(task.result?.data)
                val res = gson.toJson(task.result?.data)
                println(res[0])
                res
            }
    }
}

