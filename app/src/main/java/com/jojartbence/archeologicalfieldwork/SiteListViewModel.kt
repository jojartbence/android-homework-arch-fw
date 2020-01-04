package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository
import java.util.*


class SiteListViewModel: ViewModel() {

    val filteredSites = MutableLiveData<List<SiteModel>>(SiteRepository.findAll())


    fun filterSitesByTitle(titlePart: String) {
        filteredSites.value = SiteRepository.findAll().filter { it.title?.toLowerCase(Locale.getDefault())?.contains(titlePart.toLowerCase(Locale.getDefault())) ?: false }
    }
}