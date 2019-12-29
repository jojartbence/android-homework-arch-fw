package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jojartbence.model.Location

class SiteEditLocationViewModel : ViewModel() {

    lateinit var location: Location


    fun attachLocation(location: Location?) {
        this.location = location ?: Location()
    }


    fun getLatLng(): LatLng {
        return LatLng(location.lat, location.lng)
    }
}
