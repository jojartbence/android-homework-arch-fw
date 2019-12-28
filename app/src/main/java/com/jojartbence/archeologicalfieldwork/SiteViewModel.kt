package com.jojartbence.archeologicalfieldwork

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.jojartbence.helpers.showImagePicker
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository
import java.text.SimpleDateFormat


// TODO: find a better way to handle these 4 images. The image handling should be in the ViewModel

class SiteViewModel: ViewModel() {

    val image1RequestId = 1
    val image2RequestId = 2
    val image3RequestId = 3
    val image4RequestId = 4

    var editSite: Boolean = false
    lateinit var site: SiteModel


    fun attachArguments(site: SiteModel?, editSite: Boolean) {
        this.editSite = editSite
        this.site = site ?: SiteModel()
    }


    fun doSaveSite(title: String, description: String, visited: Boolean, dateVisitedAsString: String, additionalNotes: String) {

        // TODO: now there is always an error when there is not an appropriate date in date visited. It should not throw an error when the field is empty.

        site.title = title
        site.description = description
        site.visited = visited
        if (site.visited) {
            site.dateVisited = SimpleDateFormat("dd.MM.yyyy").parse(dateVisitedAsString)
        } else {
            site.dateVisited = null
        }
        site.additionalNotes = additionalNotes

        if (editSite) {
            SiteRepository.update(site)
        } else {
            SiteRepository.create(site)
        }
    }


    fun doDeleteSite() {
        if (!editSite) {
            SiteRepository.delete(site)
        }
    }


    fun doSelectImage(fragment: Fragment, imageNumber: Int) {
        showImagePicker(fragment, imageNumber)
    }


    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            image1RequestId -> site.images[0] = data.data.toString()
            image2RequestId -> site.images[1] = data.data.toString()
            image3RequestId -> site.images[2] = data.data.toString()
            image4RequestId -> site.images[3] = data.data.toString()
        }
    }
}