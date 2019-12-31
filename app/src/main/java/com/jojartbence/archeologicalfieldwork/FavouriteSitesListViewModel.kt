package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository

class FavouriteSitesListViewModel : ViewModel() {

    fun getFavouriteSites(): List<SiteModel> {
        return SiteRepository.findAll().filter { site -> site.isFavourite }
    }
}
