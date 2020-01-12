package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jojartbence.model.Location

class SiteNavigatorViewModel : ViewModel() {

    lateinit var siteLocation: Location


    fun attachLocation(location: Location?) {
        this.siteLocation = location ?: Location()
    }


    fun getSiteLatLng(): LatLng {
        return LatLng(siteLocation.lat, siteLocation.lng)
    }

}
