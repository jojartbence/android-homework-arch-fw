package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
            initRepository(email, context, { loginResult.value = true })
        }.addOnFailureListener {
            handleAuthenticationFailure(context, email, it)
        }
    }


    fun doSignUp(email: String, password: String, context: Context) {

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            initRepository(email, context, { loginResult.value = true })
        }.addOnFailureListener {
            handleAuthenticationFailure(context, email, it)
        }
    }


    fun initRepository(email: String, context: Context, onSuccess: () -> Unit) {
        SiteRepository.createDatabase(context, email)
        SiteRepository.fetchSites ( onSuccess )
    }


    fun closeApp(activity: FragmentActivity?): Boolean {
        activity?.finishAndRemoveTask()
        return true
    }


    private fun handleAuthenticationFailure(context: Context, email: String, it: Exception) {
        when (it.message?.contains("internal")) {
            false -> {
                errorMessage = it.message
                loginResult.value = false
            }
            true -> {
                SiteRepository.createDatabaseUsingBackup(context, email)
                SiteRepository.fetchSites { loginResult.value = true }
            }
        }
    }


    fun skipIfAlreadyLoggedIn(context: Context) {
        auth.currentUser?.email?.let {
            initRepository(it, context, { loginResult.value = true })
        }
    }
}