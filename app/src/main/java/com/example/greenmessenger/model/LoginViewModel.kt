package com.example.greenmessenger.model

import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository): ViewModel() {

    val signInExternalIntent = loginRepository.signInExternalIntent

    fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }

    fun signOut(context: Context) {
        loginRepository.signOut(context)
    }
    fun onSignInResult() {
    }

}