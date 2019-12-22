package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository

class SiteViewModel: ViewModel() {


    fun doSaveSite(site: SiteModel) {
        SiteRepository.create(site)
    }


    fun doEditSite(site: SiteModel) {
        SiteRepository.update(site)
    }


    fun doDeleteSite(site: SiteModel) {
        SiteRepository.delete(site)
    }
}