package com.example.greenmessenger.utils

import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    if (this.isEmpty())
        return false
    val pattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
    )
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isValidPass () : Boolean {
    if (this.isEmpty())
        return false
    val pattern = Pattern.compile(
        "^[A-Za-z0-9]{6,20}\$"
    )
    val matcher = pattern.matcher(this)
    return matcher.matches()
}
fun Pair <String,String>.areValidCredential() =
    this.first.isValidEmail()&& this.second.isValidPass()