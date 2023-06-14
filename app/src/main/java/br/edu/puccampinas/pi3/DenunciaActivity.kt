package br.edu.puccampinas.pi3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityDenunciaBinding
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciasBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class DenunciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDenunciaBinding
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_denuncia)
        binding = ActivityDenunciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        functions = Firebase.functions("southamerica-east1")

        binding.tvNome.text = "Denunciar ${intent.getStringExtra("nome")}"
        binding.tvMotivo.text = "Nos diga por que você escolheu denunciar o comentário ${intent.getStringExtra("comentario")}"

        binding.btnEnviar.setOnClickListener {
            enviarDisputa(user!!.uid.toString(), intent.getStringExtra("nome").toString(),
                intent.getStringExtra("comentario").toString(), binding.etMotivo.text.toString())
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                            Toast.makeText(this, "Erro ao abrir disputa, tente novamente!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Disputa aberta com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.btnVoltar.setOnClickListener {
            this.finish()
        }
    }

    private fun enviarDisputa(uid: String?, nome: String, coment: String, motivo: String): Task<String> {

        val data = hashMapOf(
            "uidDentista" to uid,
            "nome" to nome,
            "coment" to coment,
            "motivo" to motivo
        )
        return functions
            .getHttpsCallable("addDenuncia")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }
}