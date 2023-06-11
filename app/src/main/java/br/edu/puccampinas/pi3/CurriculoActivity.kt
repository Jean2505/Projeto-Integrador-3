package br.edu.puccampinas.pi3

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import br.edu.puccampinas.pi3.datastore.UserPreferencesRepository
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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class CurriculoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnVoltar: Button
    private lateinit var btnCadastrar: Button
    private lateinit var etCurriculo: EditText
    private lateinit var imgDentista: CircleImageView
    private lateinit var btnFoto: Button
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var userPreferencesRepository: UserPreferencesRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_curriculo)

        userPreferencesRepository = UserPreferencesRepository.getInstance(this)

        auth = Firebase.auth

        functions = Firebase.functions("southamerica-east1")

        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnVoltar = findViewById(R.id.btnVoltar)
        imgDentista = findViewById(R.id.imgDentista)
        btnFoto = findViewById(R.id.btnFoto)
        //etCurriculo = findViewById(R.id.etCurriculo)

        btnFoto.setOnClickListener {
            storageresult.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }
        btnCadastrar.setOnClickListener(this)
        btnVoltar.setOnClickListener(this)
    }
    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                abrirTelaDePreview()
            }else{
                Toast.makeText(this, "Você precisa permitir o uso da câmera!", Toast.LENGTH_SHORT).show()
            }
        }
    private val storageresult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this, "vou chorar", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Você precisa permitir o uso da câmera!", Toast.LENGTH_SHORT).show()
            }
        }
    private fun abrirTelaDePreview(){
        val intentCameraPreview = Intent(this, CameraPreviewActivity::class.java)
        intentCameraPreview.putExtra("IEmail", intent.getStringExtra("IEmail").toString())
        intentCameraPreview.putExtra("INome", intent.getStringExtra("INome"))
        intentCameraPreview.putExtra("ISenha", intent.getStringExtra("ISenha").toString())
        intentCameraPreview.putExtra("ITelefone", intent.getStringExtra("ITelefone"))
        intentCameraPreview.putExtra("IEnderecoUm", intent.getStringExtra("IEnderecoUm"))
        intentCameraPreview.putExtra("IEnderecoDois", intent.getStringExtra("IEnderecoDois"))
        intentCameraPreview.putExtra("IEnderecoTres", intent.getStringExtra("IEnderecoTres"))
        intentCameraPreview.putExtra("ICurriculo", intent.getStringExtra("ICurriculo"))
        startActivity(intentCameraPreview)
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
        val cv = intent.getStringExtra("ICurriculo")

        Toast.makeText(this, senha, Toast.LENGTH_SHORT).show()

        if (v!!.id == R.id.btnCadastrar) {
            val foto = enviarFoto()

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Cadastro Efetuado!" + auth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
                        uid = auth.currentUser!!.uid

                        val d = Dentista( nome, telefone, email!!, senha!!, end1, end2, end3, cv)
                        cadastrarDentista(d, uid, foto)
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
    private fun cadastrarDentista(d: Dentista, uid: String, foto: String): Task<String> {
        val fcmToken = Firebase.messaging.token.result

        Toast.makeText(this, fcmToken, Toast.LENGTH_LONG).show()
        val data = hashMapOf(
            "uid" to uid,
            "status" to true,
            "nome" to d.nome,
            "tel" to d.telefone,
            "email" to d.email,
            //"senha" to d.senha,
            "end1" to d.end1,
            "end2" to d.end2,
            "end3" to d.end3,
            "cv" to d.cv,
            "fcmToken" to fcmToken,
            "foto" to foto
        )
        return functions
            .getHttpsCallable("addDentista")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }
    private fun enviarFoto(): String {
        val foto = "gs://prijinttres.appspot.com/perfis/img-${System.currentTimeMillis()}.jpeg"
        Firebase.storage.getReference().child("perfis/img-${System.currentTimeMillis()}.jpeg")
            .putFile(File(intent.getStringExtra("fotoPerfil")).toUri())
        return foto
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Toast.makeText(this, "USUÁRIO LOGADO", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, intent.getStringExtra("IEmail"), Toast.LENGTH_LONG).show()
            Toast.makeText(this, "USUARIO SEM LOGIN", Toast.LENGTH_SHORT).show()
        }
        //Toast.makeText(this, intent.getStringExtra("fotoPerfil"), Toast.LENGTH_SHORT).show()
        if(intent.getStringExtra("fotoPerfil") != null){
            val img = intent.getStringExtra("fotoPerfil")?.let { File(it) }

            val imgBitmap = BitmapFactory.decodeFile(intent.getStringExtra("fotoPerfil"))
            //val imgBitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/media/br.edu.puccampinas.pi3/xesque.jpeg")
            imgDentista.setImageBitmap(imgBitmap)
            imgDentista.rotation = -90.0F
            imgDentista.scaleY = -1.0F

            //imgDentista.setImageURI(intent.getStringExtra("fotoPerfil")!!.toUri())
        }
    }
}
