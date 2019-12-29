package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteModel
import com.jojartbence.model.SiteRepository


class SiteListViewModel: ViewModel() {

    fun getSites(): List<SiteModel> {
        return SiteRepository.findAll()
    }


    fun doLogOut() {
        FirebaseAuth.getInstance().signOut()
        SiteRepository.clear()
    }

    fun closeApp(activity: FragmentActivity?): Boolean {
        activity?.finishAndRemoveTask()
        return true
    }
}