package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteRepository

class LoginViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val loginResult: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>(null)
    }

    val signupResult: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>(null)
    }

    var errorMessage: String? = null


    fun doLogin(email: String, password: String, context: Context) {

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            SiteRepository.createDatabase(context, email)
            SiteRepository.fetchSites { loginResult.value = true }
        }.addOnFailureListener {
            errorMessage = it.message
            loginResult.value = false
        }
    }


    fun doSignUp(email: String, password: String, context: Context) {

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            SiteRepository.createDatabase(context, email)
            SiteRepository.fetchSites { loginResult.value = true }
        }.addOnFailureListener {
            errorMessage = it.message
            signupResult.value = false
        }
    }


    fun closeApp(activity: FragmentActivity?): Boolean {
        activity?.finishAndRemoveTask()
        return true
    }

}