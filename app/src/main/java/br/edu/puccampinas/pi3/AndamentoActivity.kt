package br.edu.puccampinas.pi3

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.puccampinas.pi3.databinding.ActivityAndamentoBinding
import br.edu.puccampinas.pi3.databinding.ActivityEmergenciaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.GsonBuilder

class AndamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAndamentoBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val user = Firebase.auth.currentUser
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andamento)
        binding = ActivityAndamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverLocalizacao")

        val rv = ReceiverLocalizacao()
        registerReceiver(rv,Receiver)

        functions = Firebase.functions("southamerica-east1")


        binding.btnFinalizar.setOnClickListener {
            Toast.makeText(this, "Finalizando emergência", Toast.LENGTH_SHORT).show()
            attStatus()
            finalizarEmergencia()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                            Toast.makeText(this, "Erro ao finalizar emergência, tente novamente!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                        atualizarEmergencia("finalizada")

                        Toast.makeText(this, "Emergencia finalizada com sucesso!", Toast.LENGTH_SHORT).show()
                        val iFinaliza = Intent(this, EmergenciasActivity::class.java)
                        startActivity(iFinaliza)
                    }
                })
        }

        binding.btnEnviarL.setOnClickListener {
            Toast.makeText(this, "Enviando localização", Toast.LENGTH_SHORT).show()
            localizacaoResult.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION))
        }

        binding.btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cancelando emergência", Toast.LENGTH_SHORT).show()
            attStatus()
            atualizarEmergencia("cancelada")
            Toast.makeText(this, "Emergencia cancelada com sucesso!", Toast.LENGTH_SHORT).show()
            val iCancela = Intent(this, EmergenciasActivity::class.java)
            startActivity(iCancela)
        }
    }
    @SuppressLint("MissingPermission")
    private val localizacaoResult =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            if (location != null) {
                                enviarLocalizacao(location.latitude.toString(), location.longitude.toString())
                                    .addOnCompleteListener(OnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            val e = task.exception
                                            if (e is FirebaseFunctionsException) {
                                                val code = e.code
                                                val details = e.details
                                                Toast.makeText(this, "Erro ao enviar localização, tente novamente!", Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                                            val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)

                                            Toast.makeText(this, "Localização enviada com sucesso!", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                enviarLocalizacao(location.latitude.toString(), location.longitude.toString())
                                    .addOnCompleteListener(OnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            val e = task.exception
                                            if (e is FirebaseFunctionsException) {
                                                val code = e.code
                                                val details = e.details
                                                Toast.makeText(this, "Erro ao enviar localização, tente novamente!", Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                                            val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)

                                            Toast.makeText(this, "Localização enviada com sucesso!", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                } else -> {
                Toast.makeText(this, "Você precisa aceitar o uso da localização!", Toast.LENGTH_SHORT).show()
            }
            }
        }

    private fun enviarLocalizacao(lat: String, long: String): Task<String> {
        
        val data = hashMapOf(
            "id" to intent.getStringExtra("emergencia"),
            "lat" to lat,
            "long" to long
        )
        return functions
            .getHttpsCallable("enviaLocalizacaoDentista")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }

    private fun finalizarEmergencia(): Task<String> {

        val data = hashMapOf(
            "emerg" to intent.getStringExtra("emergencia"),
            "uid" to user!!.uid
        )
        return functions
            .getHttpsCallable("finalizaEmergencia")
            .call(data)
            .continueWith { task ->
                val res = gson.toJson(task.result?.data)
                res
            }
    }

    private fun atualizarEmergencia(stat: String){
        db.collection("emergencias").document(intent.getStringExtra("emergencia")!!)
            .update("status", stat)
    }

    private fun attStatus() {
        db.collection("dentistas").whereEqualTo("email", user!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    db.collection("dentistas").document(document.id)
                        .update("status", true)
                }
            }
    }

    inner class ReceiverLocalizacao: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val iMapa = Intent(context, MapsActivity::class.java)
            iMapa.putExtra("lat", intent.getStringExtra("lat"))
            iMapa.putExtra("long", intent.getStringExtra("long"))
            startActivity(iMapa)

        }
    }
}