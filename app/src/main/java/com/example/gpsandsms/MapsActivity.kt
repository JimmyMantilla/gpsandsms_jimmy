package com.example.gpsandsms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.concurrent.fixedRateTimer

class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val MIN_TIME: Long = 1000
    private val MIN_DIST: Long = 5
    private var latLng: LatLng? = null
    var seleccionintervalo: String = ""
    var telefono: String = ""
    var delay: Long = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED)
        //val intent: Intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        recibirdatos()
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    locationListener = object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            try {
                                latLng = LatLng(location.latitude, location.longitude)
                                mMap!!.addMarker(MarkerOptions().position(latLng!!).title("My position"))
                                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                                val myLatitude = location.latitude.toString()
                                val myLongitude = location.longitude.toString()
                                val message = "Latitude = $myLatitude Longitude = $myLongitude"
                                val smsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage(telefono, null, message, null, null)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    }
                    locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                    try {
                        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST.toFloat(), locationListener)
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST.toFloat(), locationListener)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    handler.postDelayed(this, delay)//1 sec delay
                }
            }, 0)
    }
    private fun recibirdatos() {
        val objintent: Intent = intent
        seleccionintervalo = objintent.getStringExtra("seleccionintervalo")
        telefono = objintent.getStringExtra("telefono")
        delay = seleccionintervalo.toLong()
    }
}