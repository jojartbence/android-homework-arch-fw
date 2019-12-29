package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteRepository

class SettingsViewModel : ViewModel() {

    fun getLoggedInAs(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }


    fun getTotalSites(): Int {
        return SiteRepository.findAll().size
    }


    fun getVisitedSites(): Int {
        return SiteRepository.findAll().filter { it.visited }.size
    }
}