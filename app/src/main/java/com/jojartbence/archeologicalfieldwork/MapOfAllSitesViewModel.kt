package com.jojartbence.archeologicalfieldwork

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jojartbence.helpers.readImageFromPath
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository

class MapOfAllSitesViewModel : ViewModel() {

    val selectedSite = MutableLiveData<SiteModel>()


    fun getAllSites(): List<SiteModel> {
        return SiteRepository.findAll()
    }


    fun selectSiteById(id: Long) {
        selectedSite.value = SiteRepository.findById(id)
    }


    fun getImageOfSelectedSite(context: Context): Bitmap? {
        return readImageFromPath(context, selectedSite.value?.images?.get(0))
    }

}
