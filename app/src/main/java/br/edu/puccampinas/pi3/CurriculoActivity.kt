package br.edu.puccampinas.pi3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.Token
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class CurriculoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnVoltar: Button
    private lateinit var btnCadastrar: Button
    private lateinit var etCurriculo: EditText

    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriculo)

        auth = Firebase.auth

        functions = Firebase.functions("southamerica-east1")

        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnVoltar = findViewById(R.id.btnVoltar)
        etCurriculo = findViewById(R.id.etCurriculo)


        btnCadastrar.setOnClickListener(this)
        btnVoltar.setOnClickListener(this)
    }
    private lateinit var uid: String
    override fun onClick(v: View?) {

        val nome = intent.getStringExtra("INome")
        val email = intent.getStringExtra("IEmail").toString()
        val senha = intent.getStringExtra("ISenha").toString()
        val telefone = intent.getStringExtra("ITelefone")
        val end1 = intent.getStringExtra("IEnderecoUm")
        val end2 = intent.getStringExtra("IEnderecoDois")
        val end3 = intent.getStringExtra("IEnderecoTres")
        val cv = etCurriculo.text.toString()

        Toast.makeText(this, senha, Toast.LENGTH_SHORT).show()

        if (v!!.id == R.id.btnCadastrar) {


            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Cadastro Efetuado!" + auth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
                        uid = auth.currentUser!!.uid

                        val d = Dentista( nome, telefone, email!!, senha!!, end1, end2, end3, cv)
                        cadastrarDentista(d, uid)
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    val e = task.exception
                                    if (e is FirebaseFunctionsException) {
                                        val code = e.code
                                        val details = e.details
                                        Snackbar.make(btnCadastrar, "Erro no Cadastro. Tente Novamente.",
                                            Snackbar.LENGTH_LONG).show();
                                    }
                                }else{

                                    val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                                    val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)

                                    Snackbar.make(btnCadastrar, "Cadastro efetuado com sucesso!",
                                        2000).show();
                                }
                            })

                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(btnCadastrar, "Erro ao cadastrar!" + task.exception.toString(),
                            10000).show()
                    }
                }

        } else if (v!!.id == R.id.btnVoltar) {
            val intentVoltar = Intent(this, MainActivity::class.java)
            this.startActivity(intentVoltar)
        }
    }
    private fun cadastrarDentista(d: Dentista, uid: String): Task<String> {

        val data = hashMapOf(
            "uid" to uid,
            "status" to true,
            "nome" to d.nome,
            "tel" to d.telefone,
            "email" to d.email,
            "senha" to d.senha,
            "end1" to d.end1,
            "end2" to d.end2,
            "end3" to d.end3,
            "cv" to d.cv
        )
        return functions
            .getHttpsCallable("addDentista")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "USUÁRIO LOGADO", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "USUARIO SEM LOGIN", Toast.LENGTH_SHORT).show()
        }
    }
}