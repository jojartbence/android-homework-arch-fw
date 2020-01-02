package com.jojartbence.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SiteFirebaseStore: SiteStoreInterface {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference


    override fun findAll(): List<SiteModel> {
        return sites
    }


    override fun findById(id: Long): SiteModel? {
        return sites.find { p -> p.id == id }
    }


    override fun create(site: SiteModel) {
        val key = db.child("users").child(userId).child("sites").push().key
        key?.let {
            site.firebaseId = key
            sites.add(site)
            db.child("users").child(userId).child("sites").child(key).setValue(site)
        }
    }


    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.firebaseId == site.firebaseId }
        if (foundSite != null) {
            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.location = site.location
            foundSite.images = site.images.clone()
            foundSite.visited = site.visited
            foundSite.dateVisited = site.dateVisited
            foundSite.additionalNotes = site.additionalNotes
            foundSite.isFavourite = site.isFavourite
            foundSite.rating = site.rating
        }

        db.child("users").child(userId).child("sites").child(site.firebaseId).setValue(site)

    }


    override fun delete(site: SiteModel) {
        db.child("users").child(userId).child("sites").child(site.firebaseId).removeValue()
        sites.remove(sites.find { it.firebaseId == site.firebaseId })
    }


    override fun clear() {
        sites.clear()
    }


    fun fetchSites(sitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(sites) { it.getValue<SiteModel>(SiteModel::class.java) }
                sitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        sites.clear()
        db.child("users").child(userId).child("sites").addListenerForSingleValueEvent(valueEventListener)
    }

}