package com.jojartbence.archeologicalfieldwork

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.jojartbence.helpers.checkLocationPermissions
import com.jojartbence.helpers.showImagePicker
import com.jojartbence.model.*
import java.text.SimpleDateFormat


// TODO: find a better way to handle these 4 images. The image handling should be in the ViewModel

class SiteViewModel: ViewModel() {

    val image1RequestId = 1
    val image2RequestId = 2
    val image3RequestId = 3
    val image4RequestId = 4

    var editSite: Boolean = false
    lateinit var site: SiteModel

    val visitedSwitchState = MutableLiveData<Boolean>(false)

    val liveLocation = MutableLiveData<Location> ()


    fun attachArguments(site: SiteModel?, editSite: Boolean) {
        this.editSite = editSite
        this.site = site ?: SiteModel()
    }


    fun doSaveSite(title: String, description: String, visited: Boolean, dateVisitedAsString: String, additionalNotes: String, rating: Float) {

        site.title = title
        site.description = description
        site.visited = visited
        if (site.visited) {
            site.dateVisited = SimpleDateFormat("dd.MM.yyyy").parse(dateVisitedAsString)
        } else {
            site.dateVisited = null
        }
        site.additionalNotes = additionalNotes
        site.rating = rating

        if (editSite) {
            SiteRepository.update(site)
        } else {
            SiteRepository.create(site)
        }
    }


    fun doDeleteSite() {
        if (editSite) {
            SiteRepository.delete(site)
        }
    }


    fun doSelectImage(fragment: Fragment, imageNumber: Int) {
        showImagePicker(fragment, imageNumber)
    }


    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val chosenImageContainer: SiteModel.ImageContainer = when (requestCode) {
            image1RequestId -> site.imageContainerList[0]
            image2RequestId -> site.imageContainerList[1]
            image3RequestId -> site.imageContainerList[2]
            image4RequestId -> site.imageContainerList[3]
            else -> SiteModel.ImageContainer()
        }

        chosenImageContainer.memoryPath = data.data.toString()
        chosenImageContainer.updateNeeded = true
    }


    fun initLocationService(activity: Activity) {
        if (!editSite) {
            if (checkLocationPermissions(activity)) {
                var locationService = LocationServices.getFusedLocationProviderClient(activity)
                locationService.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        site.location = (Location(it.latitude, it.longitude, 15f))
                        liveLocation.value = site.location
                    }
                }
            }
        }
    }


    fun shareSiteInEmail(parent: Fragment) {
        // TODO: share the images as an attachment
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, "A site has been shared with you")
            putExtra(Intent.EXTRA_TEXT, site.toEmailText())
        }
        if (intent.resolveActivity(parent.activity!!.packageManager) != null) {
            parent.startActivity(intent)
        }
    }
}