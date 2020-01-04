package com.jojartbence.model

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jojartbence.helpers.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File

class SiteFirebaseStore(val context: Context): SiteStoreInterface {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    val db = FirebaseDatabase.getInstance().reference
    val st = FirebaseStorage.getInstance().reference



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
            updateImages(site)
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
        updateImages(site)
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


    fun updateImages(site: SiteModel) {
        // TODO: the code is not so nice, val index should be avoided. Maybe introduce a function instead of this that creates uploads the images and changes path to url in the SiteModel.

        site.images.withIndex().forEach {

            var imagePath = it.value
            val index = it.index

            if (imagePath != "") {
                val fileName = File(imagePath)
                val imageName = fileName.name

                var imageRef = st.child(userId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, imagePath)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            site.images[index] = it.toString()
                            db.child("users").child(userId).child("sites").child(site.id)
                                .setValue(site)
                        }
                    }
                }
            }
        }
    }

}