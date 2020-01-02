package com.jojartbence.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SiteFirebaseStore: SiteStoreInterface {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    val db = FirebaseDatabase.getInstance().reference


    override fun findAll(): List<SiteModel> {
        return sites
    }


    override fun findById(id: String): SiteModel? {
        return sites.find { p -> p.id == id }
    }


    override fun create(site: SiteModel) {
        val key = db.child("users").child(userId).child("sites").push().key
        key?.let {
            site.id = key
            sites.add(site)
            db.child("users").child(userId).child("sites").child(key).setValue(site)
        }
    }


    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.id == site.id }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.location = site.location
            foundSite.images = site.images.toMutableList()
            foundSite.visited = site.visited
            foundSite.dateVisited = site.dateVisited
            foundSite.additionalNotes = site.additionalNotes
            foundSite.isFavourite = site.isFavourite
            foundSite.rating = site.rating
        }

        db.child("users").child(userId).child("sites").child(site.id).setValue(site)

    }


    override fun delete(site: SiteModel) {
        db.child("users").child(userId).child("sites").child(site.id).removeValue()
        sites.remove(sites.find { it.id == site.id })
    }


    override fun clear() {
        sites.clear()
    }


    fun fetchSites(onSitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(sites) { it.getValue<SiteModel>(SiteModel::class.java) }
                onSitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        sites.clear()
        db.child("users").child(userId).child("sites").addListenerForSingleValueEvent(valueEventListener)
    }

}