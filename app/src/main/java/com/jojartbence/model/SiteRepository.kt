package com.jojartbence.model

import android.content.Context


object SiteRepository {
    private lateinit var siteStore: SiteStoreInterface
    private var backupSiteStore: SiteStoreInterface? = null


    fun createDatabase(context: Context, userEmail: String) {
        siteStore = SiteFirebaseStore(context)
        backupSiteStore = SiteJsonStore(context, userEmail)
    }


    fun fetchSites(onSitesReady: () -> Unit) {
        siteStore.fetchSites(onSitesReady)
    }


    fun findAll(): List<SiteModel> {
        return siteStore.findAll()
    }


    fun create(site: SiteModel) {
        siteStore.create(site)
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


    private fun initBackupStore() {
        siteStore.findAll().forEach {
            if (backupSiteStore.findById(it.id) != null)
        }
    }
}