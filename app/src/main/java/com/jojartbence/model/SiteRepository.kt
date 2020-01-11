package com.jojartbence.model

import android.content.Context


object SiteRepository {
    private lateinit var siteStore: SiteStoreInterface
    private var backupSiteStore: SiteStoreInterface? = null


    fun createDatabase(context: Context, userEmail: String) {
        siteStore = SiteFirebaseStore(context)
        backupSiteStore = SiteJsonStore(context, userEmail)
    }


    fun createDatabaseUsingBackup(context: Context, userEmail: String) {
        siteStore = SiteJsonStore(context, userEmail)
    }


    fun fetchSites(onSitesReady: () -> Unit) {
        siteStore.fetchSites {
            onSitesReady()
            backupSiteStore?.initAsBackupStore(siteStore)
        }
    }


    fun findAll(): List<SiteModel> {
        return siteStore.findAll()
    }


    fun create(site: SiteModel) {
        siteStore.create(site)
        backupSiteStore?.create(site)
    }


    fun update(site: SiteModel) {
        siteStore.update(site)
        backupSiteStore?.update(site)
    }


    fun delete(site: SiteModel) {
        siteStore.delete(site)
        backupSiteStore?.delete(site)
    }


    fun findById(id: String) : SiteModel? {
        return siteStore.findById(id)
    }


    fun clear() {
        siteStore.clear()
        backupSiteStore?.clear()
    }
}