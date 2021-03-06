package com.jojartbence.model

import android.content.Context
import android.provider.ContactsContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jojartbence.helpers.exists
import com.jojartbence.helpers.read
import com.jojartbence.helpers.write
import java.util.*


const val JSON_FILE_BASE = "sites.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<SiteModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SiteJsonStore: SiteStoreInterface {
    val context: Context
    var sites = mutableListOf<SiteModel>()

    var jsonFileName: String


    constructor (context: Context, userEmail: String) {
        this.context = context
        jsonFileName = userEmail + JSON_FILE_BASE
    }


    override fun findAll(): MutableList<SiteModel> {
        return sites
    }


    override fun create(site: SiteModel) {
        site.id = site.id ?: generateRandomId().toString()
        sites.add(site)
        serialize()
    }


    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { s -> s.id == site.id }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.location = site.location
            foundSite.imageContainerList = site.imageContainerList.map {it.copy()}
            foundSite.visited = site.visited
            foundSite.dateVisited = site.dateVisited
            foundSite.additionalNotes = site.additionalNotes
            foundSite.isFavourite = site.isFavourite
            foundSite.rating = site.rating

            serialize()
        }
    }


    override fun delete(site: SiteModel) {
        sites.remove(sites.find { it.id == site.id })
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sites,
            listType
        )
        write(context, jsonFileName, jsonString)
    }


    private fun deserialize() {
        val jsonString = read(context, jsonFileName)
        sites = Gson().fromJson(jsonString,
            listType
        )
    }


    override fun findById (id: String): SiteModel? {
        return sites.find { it.id == id }
    }


    override fun clear() {
        sites.clear()
    }


    override fun fetchSites(onSitesReady: () -> Unit) {
        if (exists(context, jsonFileName)) {
            deserialize()
        }
        onSitesReady()
    }
}