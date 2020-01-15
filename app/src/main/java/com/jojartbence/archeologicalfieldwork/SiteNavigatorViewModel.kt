package com.jojartbence.archeologicalfieldwork

import android.app.Activity
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.jojartbence.helpers.checkLocationPermissions
import com.jojartbence.model.Location

class SiteNavigatorViewModel : ViewModel() {

    lateinit var siteLocation: Location
    lateinit var siteTitle: String

    val liveLocation = MutableLiveData<LatLng> ()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest



    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult?.lastLocation
            location ?: return
            liveLocation.value = (LatLng(location.latitude, location.longitude))
        }
    }


    fun attachLocation(location: Location?) {
        this.siteLocation = location ?: Location()
    }


    fun attachTitle(title: String) {
        this.siteTitle = title
    }


    fun initLocationService(activity: Activity) {
        if (checkLocationPermissions(activity)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            getSingleLocation()
            createPeridodicLocationRequest()
        }
    }


    private fun getSingleLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                liveLocation.value = (LatLng(it.latitude, it.longitude))
            }
        }
    }


    private fun createPeridodicLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
