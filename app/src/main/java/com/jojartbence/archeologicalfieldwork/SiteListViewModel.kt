package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jojartbence.model.SiteJsonStore
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteStoreInterface

class SiteListViewModel: ViewModel() {

    lateinit var siteStore: SiteStoreInterface

    fun createDatabase(context: Context) {
        siteStore = SiteJsonStore(context)
    }

    fun getSites(): List<SiteModel> {
        return siteStore.findAll()
    }

    fun doAddSite() {
        // TODO
    }

    fun doEditSite(site: SiteModel) {
        // TODO
    }
}