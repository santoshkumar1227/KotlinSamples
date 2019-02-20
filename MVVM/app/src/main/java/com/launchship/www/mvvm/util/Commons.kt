package com.launchship.www.mvvm.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.widget.EditText
import android.view.Display


object Commons {
    val acNo = "123456789"
    var balance = 50000

    fun readFromEditText(editText: EditText): String {
        return editText.text.toString()
    }

    fun getWidthOfScreen(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }


}