package com.launchship.www.mvvm.util

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.lang.Exception

object Extensions {
    fun AppCompatActivity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun String.toInteger(): Int {
        return try {
            this.toInt()
        } catch (e: Exception) {
            0
        }
    }
}