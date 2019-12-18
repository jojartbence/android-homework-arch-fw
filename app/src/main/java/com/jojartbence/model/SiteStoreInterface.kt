package com.jojartbence.model

interface SiteStoreInterface {
    fun findAll(): List<SiteModel>
    fun create(site: SiteModel)
    fun update(site: SiteModel)
    fun delete(site: SiteModel)
    fun findById(id:Long) : SiteModel?
    fun clear()
}