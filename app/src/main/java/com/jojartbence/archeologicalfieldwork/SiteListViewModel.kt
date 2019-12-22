package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository


class SiteListViewModel: ViewModel() {

    fun createDatabase(context: Context) {
        SiteRepository.createDatabase(context)
    }


    fun getSites(): List<SiteModel> {
        return SiteRepository.findAll()
    }


    fun doLogOut() {
        FirebaseAuth.getInstance().signOut()
        SiteRepository.clear()
    }
}