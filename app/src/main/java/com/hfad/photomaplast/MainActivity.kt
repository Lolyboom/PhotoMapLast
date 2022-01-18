package com.hfad.photomaplast

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hfad.photomaplast.fragments.CameraFragment
import com.hfad.photomaplast.databinding.ActivityMainBinding
import com.hfad.photomaplast.fragments.MapsFragment

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var viewModel: RoomDbViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location

    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        databaseReference = database?.reference?.child("profile")
        database = FirebaseDatabase.getInstance()
        setContentView(binding.root)

        replaceFragment(fragment = MapsFragment())
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
                 return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { Location ->

           if(Location != null) {
               lastLocation = Location
               val currentLatLong = LatLng(Location.latitude, Location.longitude)
               map.addMarker(MarkerOptions().position(currentLatLong).title("My position"))
               map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
           }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        map.addMarker(markerOptions)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
           android.R.id.home -> replaceFragment(MapsFragment())
           R.id.camera -> {
               replaceFragment(CameraFragment())
           }
           R.id.logOut -> {
               logoutDialog()
           }
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    private fun logoutProfile() {
            auth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
           }
    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Photo Map")
        builder.setMessage("Exit the app?")
        builder.setNegativeButton("No"){dialog, i ->
        }
        builder.setPositiveButton("Yes"){dialog, i ->
            logoutProfile()
        }
        builder.show()
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentHolder, fragment)
            .commit()
    }
    override fun onMarkerClick(p0: Marker?) = false
}