package com.example.santoshb.kotlindemo.util

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun String.stringEquals(string: String = ""): Boolean {
    return this == string
}

fun String.isEmail(): Boolean {
    return (!TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches())
}

fun String.getDate(): String {
    val df = SimpleDateFormat("dd:MM:yyyy HH:mm:ss")
    return df.format(Calendar.getInstance().time)
}