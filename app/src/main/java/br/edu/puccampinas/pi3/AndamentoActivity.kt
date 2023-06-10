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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andamento)

        binding = ActivityAndamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Receiver = IntentFilter("br.edu.puccampinas.pi3.RecieverLocalizacao")

        val rv = ReceiverLocalizacao()
        registerReceiver(rv,Receiver)

        functions = Firebase.functions("southamerica-east1")


        binding.btnEnviarL.setOnClickListener {
            localizacaoResult.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
    @SuppressLint("MissingPermission")
    private val localizacaoResult =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(this, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
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
                    // Only approximate location access granted.
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(this, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
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
                // No location access granted.
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

    inner class ReceiverLocalizacao: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
            //Toast.makeText(context, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show()
            val iMapa = Intent(context, MapsActivity::class.java)
            iMapa.putExtra("lat", intent.getStringExtra("lat"))
            iMapa.putExtra("long", intent.getStringExtra("long"))
            startActivity(iMapa)
            Toast.makeText(context, intent.getStringExtra("lat"), Toast.LENGTH_SHORT).show()
        }
    }

}