package com.jojartbence.archeologicalfieldwork

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.jojartbence.helpers.showImagePicker
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository

class SiteViewModel: ViewModel() {

    val image1RequestId = 1
    val image2RequestId = 2
    val image3RequestId = 3
    val image4RequestId = 4


    fun doSaveSite(site: SiteModel) {
        SiteRepository.create(site)
    }


    fun doEditSite(site: SiteModel) {
        SiteRepository.update(site)
    }


    fun doDeleteSite(site: SiteModel) {
        SiteRepository.delete(site)
    }


    fun doSelectImage(fragment: Fragment, imageNumber: Int) {
        showImagePicker(fragment, imageNumber)
    }


    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent, site: SiteModel) {
        when (requestCode) {
            image1RequestId -> site.images[0] = data.data.toString()
            image2RequestId -> site.images[1] = data.data.toString()
            image3RequestId -> site.images[2] = data.data.toString()
            image4RequestId -> site.images[3] = data.data.toString()
        }
    }
}