package br.edu.puccampinas.pi3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.puccampinas.pi3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maps)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.map, mapFragment)
            .commit()*/

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            addMarkers(googleMap)
        }

        binding.btnVoltar.setOnClickListener {
            Toast.makeText(this, "voltar", Toast.LENGTH_SHORT).show()
            this.finish()
        }




    }
    val teste = LatLng(-22.944245,-47.009150)
    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }

    data class Place(
        val name: String,
        val latLng: LatLng,
        val address: String,
        val rating: Float
    )

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title("Emergência")
                    .position(teste)
            )
        }
    }
}