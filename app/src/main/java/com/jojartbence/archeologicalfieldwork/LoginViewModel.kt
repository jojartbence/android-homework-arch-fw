package com.jojartbence.archeologicalfieldwork

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteRepository

class LoginViewModel: ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun doLogin(email: String, password: String,
                doOnSuccess: () -> Unit, doOnFailure: (Exception) -> Unit, doOnComplete: () -> Unit) {

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            doOnSuccess()
        }.addOnFailureListener {
            doOnFailure(it)
        }.addOnCompleteListener {
            doOnComplete()
        }
    }


    fun doSignUp(email: String, password: String,
                 doOnSuccess: () -> Unit, doOnFailure: (Exception) -> Unit, doOnComplete: () -> Unit) {

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            doOnSuccess()
        }.addOnFailureListener {
            doOnFailure(it)
        }.addOnCompleteListener {
            doOnComplete()
        }
    }

    fun createDatabase(context: Context) {
        SiteRepository.createDatabase(context, auth.currentUser?.email ?: "dummy")
    }

}