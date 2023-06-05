package com.example.greenmessenger.model

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LoginRepository @Inject constructor(private val context: Context) {

    init {
        FirebaseApp.initializeApp(context)
    }

    private val auth = Firebase.auth

    val signInExternalIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
            listOf(
                AuthUI.IdpConfig.AnonymousBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
        ).build()


    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

    fun isUserGuest(): Boolean {
        return auth.currentUser?.isAnonymous ?: true
    }

    fun signOut(context: Context) {
        AuthUI.getInstance().signOut(context)
    }

}