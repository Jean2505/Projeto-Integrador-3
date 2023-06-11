package br.edu.puccampinas.pi3

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
    //private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
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

        db.collection("avaliacoes").whereEqualTo("uidDentista", user!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    val avaliacao = Avaliacoes(nome = document["nome"].toString(),
                                                comentario = document["coment"].toString(),
                                                estrela = document["aval"] as Long)
                    println(avaliacao)
                    lista.add(avaliacao)
                    println(lista[0])

                    val avalAdapter = AvaliacoesAdapter(lista)
                    val recyclerView: RecyclerView = findViewById(R.id.rvAvaliacoes)
                    recyclerView.adapter = avalAdapter
                }
            }

        btnChamadas.setOnClickListener{
            this.finish()
        }
    }

    /*private fun getAvaliacoes(): Task<String> {

        val data = hashMapOf(
            "uid" to user!!.uid
        )
        return functions
            .getHttpsCallable("getAvaliacoes")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }*/
}

