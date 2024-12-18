package com.example.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the SupportMapFragment and get notified when the map is ready
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment

        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            Toast.makeText(this, "Map fragment not found!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker at Sydney and move the camera
        val sydney = LatLng(-33.852, 151.211)
        mMap?.apply {
            //addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))

            // Change map type
            mapType = GoogleMap.MAP_TYPE_SATELLITE
            mapType = GoogleMap.MAP_TYPE_HYBRID
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        // Enable My Location if permission is granted
        enableMyLocationIfPermitted()
    }

    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            mMap?.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap?.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(
                    this,
                    "Location permission not granted, location layer disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
