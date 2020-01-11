package com.jojartbence.model


interface SiteStoreInterface {
    fun findAll(): List<SiteModel>
    fun create(site: SiteModel)
    fun update(site: SiteModel)
    fun delete(site: SiteModel)
    fun findById(id: String): SiteModel?
    fun clear()
    fun fetchSites(onSitesReady: () -> Unit)

    fun initAsBackupStore(primaryStore: SiteStoreInterface) {
        primaryStore.findAll().forEach {
            when (findById(it.id)) {
                is SiteModel -> update(it)
                else -> create(it)
            }
        }
    }
}