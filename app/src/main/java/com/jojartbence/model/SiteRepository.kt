package com.jojartbence.model


object SiteRepository {
    private lateinit var siteStore: SiteStoreInterface


    fun createDatabase() {
        siteStore = SiteFirebaseStore()
    }


    fun fetchSites(onSitesReady: () -> Unit) {
        (siteStore as SiteFirebaseStore).fetchSites { onSitesReady }
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


    fun findById(id:Long) : SiteModel? {
        return siteStore.findById(id)
    }


    fun clear() {
        siteStore.clear()
    }
}