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
            val site = it
            site.id?.let {
                when (findById(it)) {
                    is SiteModel -> update(site)
                    else -> create(site)
                }
            }
        }
    }
}