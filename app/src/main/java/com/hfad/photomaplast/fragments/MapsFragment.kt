package com.hfad.photomaplast.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hfad.photomaplast.R

class MapsFragment : Fragment()  {

    lateinit var map: GoogleMap

    // открытие карты с маркером в текущем местоположении
    private val callback = OnMapReadyCallback { googleMap ->
        val myLatitude = 12.0
        val myLongitude = 15.0
        val myLocation = LatLng(myLatitude, myLongitude)
        googleMap.addMarker(MarkerOptions()
            .position(myLocation)
            .title("My Location")

        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,5f))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



}