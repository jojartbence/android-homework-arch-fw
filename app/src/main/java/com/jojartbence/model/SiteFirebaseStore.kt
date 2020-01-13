package com.jojartbence.model

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.jojartbence.helpers.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File

class SiteFirebaseStore(val context: Context): SiteStoreInterface {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    private val database = FirebaseDatabase.getInstance()
    private val imageStore = FirebaseStorage.getInstance()


    init {
        database.setPersistenceEnabled(true)
    }


    override fun findAll(): List<SiteModel> {
        return sites
    }


    override fun findById(id: String): SiteModel? {
        return sites.find { p -> p.id == id }
    }


    override fun create(site: SiteModel) {
        val key = database.reference.child("users").child(userId).child("sites").push().key
        key?.let {
            site.id = key
            sites.add(site)
            database.reference.child("users").child(userId).child("sites").child(key).setValue(site)
            updateImages(site)
        }
    }


    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.id == site.id }
        if (foundSite != null) {

            // Only deleting images thats path has changed.
            deleteImagesFromCloud(foundSite.imageContainerList.filter {it.updateNeeded})

            foundSite.title = site.title
            foundSite.description = site.description
            foundSite.location = site.location
            foundSite.imageContainerList = site.imageContainerList.map {it.copy()}
            foundSite.visited = site.visited
            foundSite.dateVisited = site.dateVisited
            foundSite.additionalNotes = site.additionalNotes
            foundSite.isFavourite = site.isFavourite
            foundSite.rating = site.rating
        }

        site.id?.let {
            database.reference.child("users").child(userId).child("sites").child(it).setValue(site)
        }

        updateImages(site)
    }


    override fun delete(site: SiteModel) {
        site.id?.let {
            database.reference.child("users").child(userId).child("sites").child(it).removeValue()
        }
        deleteImagesFromCloud(site.imageContainerList)
        sites.remove(sites.find { it.id == site.id })
    }


    override fun clear() {
        sites.clear()
    }


    override fun fetchSites(onSitesReady: () -> Unit) {
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
        database.reference.child("users").child(userId).child("sites").addListenerForSingleValueEvent(valueEventListener)
    }


    private fun updateImages(site: SiteModel) {

        site.imageContainerList.forEach {

            var container = it

            if (container.updateNeeded) {
                val fileName = File(container.memoryPath)
                val imageName = fileName.name

                var imageRef = imageStore.reference.child(userId + '/' + site.id + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, container.memoryPath)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            container.url = it.toString()
                            site.id?.let {
                                database.reference.child("users").child(userId).child("sites").child(it).setValue(site)
                            }
                            container.updateNeeded = false
                        }
                    }
                }
            }
        }
    }


    private fun deleteImagesFromCloud(images: List<SiteModel.ImageContainer>) {
        images.mapNotNull {imageContainer -> imageContainer.url}.filter { it.startsWith("http") }.forEach {
            try {
                FirebaseStorage.getInstance().getReferenceFromUrl(it).delete()
            } catch (e: Exception) {
                // TODO: handling delete errors
            }
        }
    }

}