package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

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

}