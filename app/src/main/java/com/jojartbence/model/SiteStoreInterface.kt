package com.jojartbence.model

interface SiteStoreInterface {
    fun findAll(): List<SiteModel>
    fun create(placemark: SiteModel)
    fun update(placemark: SiteModel)
    fun delete(placemark: SiteModel)
    fun findById(id:Long) : SiteModel?
    fun clear()
}