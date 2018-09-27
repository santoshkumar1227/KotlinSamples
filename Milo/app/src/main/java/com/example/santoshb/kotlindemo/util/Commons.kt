package com.example.santoshb.kotlindemo.util

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.example.santoshb.kotlindemo.`interface`.BooleanCallback

class Commons {
    companion object {
        fun readFromEditText(editText: EditText?): String = (editText?.text.toString())
        fun isEmptyEditText(editText: EditText?): Boolean = readFromEditText(editText).isEmpty()
        fun showValidationAlertDialog(context: Context,
                                      message: String,
                                      booleanCallback: BooleanCallback? = null) {
            val dialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setNegativeButton("ok") { _, _ ->
                        booleanCallback?.booleanCallback(true)
                    }
            dialog.show()
        }

        fun showConfirmationAlertDialog(context: Context,
                                        message: String,
                                        booleanCallback: BooleanCallback) {
            val dialog = AlertDialog.Builder(context)
                    .setMessage(message)
                    .setNegativeButton("cancel") { _, _ -> }
                    .setPositiveButton("ok") { _, _ ->
                        booleanCallback.booleanCallback(true)
                    }
            dialog.show()
        }
    }
}