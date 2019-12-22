package com.jojartbence.model

import android.content.Context

object SiteRepository {
    private lateinit var siteStore: SiteStoreInterface


    fun create(context: Context) {
        siteStore = SiteJsonStore(context)
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