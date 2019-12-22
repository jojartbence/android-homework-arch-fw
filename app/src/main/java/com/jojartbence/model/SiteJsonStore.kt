package com.jojartbence.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jojartbence.helpers.exists
import com.jojartbence.helpers.read
import com.jojartbence.helpers.write
import java.util.*


const val JSON_FILE = "sites.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<SiteModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SiteJsonStore: SiteStoreInterface {
    val context: Context
    var sites = mutableListOf<SiteModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<SiteModel> {
        return sites
    }

    override fun create(site: SiteModel) {
        site.id = generateRandomId()
        sites.add(site)
        serialize()
    }


    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { s -> s.id == site.id }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.location = site.location
            foundSite.images = site.images.clone()
            foundSite.visited = site.visited
            foundSite.visitDate = site.visitDate
            foundSite.additionalNotes = site.additionalNotes

            serialize()
        }
    }

    override fun delete(site: SiteModel) {
        sites.remove(site)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sites,
            listType
        )
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        sites = Gson().fromJson(jsonString,
            listType
        )
    }

    override fun findById(id:Long) : SiteModel? {
        return sites.find { it.id == id }
    }

    override fun clear() {
        sites.clear()
    }

}