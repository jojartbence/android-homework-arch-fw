package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jojartbence.model.SiteJsonStore
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository
import com.jojartbence.model.SiteStoreInterface

class SiteListViewModel: ViewModel() {


    fun createDatabase(context: Context) {
        SiteRepository.createDatabase(context)
    }

    fun getSites(): List<SiteModel> {
        return SiteRepository.findAll()
    }

    fun doAddSite() {
        // TODO
    }

    fun doEditSite(site: SiteModel) {
        // TODO
    }
}