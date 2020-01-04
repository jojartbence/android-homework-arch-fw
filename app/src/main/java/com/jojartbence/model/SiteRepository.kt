package com.jojartbence.model

import android.content.Context


object SiteRepository {
    private lateinit var siteStore: SiteFirebaseStore


    fun createDatabase(context: Context) {
        siteStore = SiteFirebaseStore(context)
    }


    fun fetchSites(onSitesReady: () -> Unit) {
        siteStore.fetchSites (onSitesReady)
    }


    fun findAll(): List<SiteModel> {
        return siteStore.findAll()
    }


    fun create(site: SiteModel) {
        return siteStore.create(site)
    }


    fun update(site: SiteModel) {
        siteStore.update(site)
    }


    fun delete(site: SiteModel) {
        siteStore.delete(site)
    }


    fun findById(id: String) : SiteModel? {
        return siteStore.findById(id)
    }


    fun clear() {
        siteStore.clear()
    }
}