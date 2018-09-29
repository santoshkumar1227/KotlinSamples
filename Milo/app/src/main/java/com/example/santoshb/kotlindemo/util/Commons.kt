package com.example.santoshb.kotlindemo.util

import android.content.Context
import android.os.Build
import android.support.v7.app.AlertDialog
import android.text.Html
import android.widget.EditText
import com.example.santoshb.kotlindemo.`interface`.BooleanCallback

class Commons {
    companion object {
        fun readFromEditText(editText: EditText?): String = (editText?.text.toString())
        fun isEmptyEditText(editText: EditText?): Boolean = readFromEditText(editText).isEmpty()
        fun showValidationAlertDialog(context: Context,
                                      message: String,
                                      booleanCallback: BooleanCallback? = null,
                                      cancelable: Boolean = true, title: String = "") {
            val dialog =
                    AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(message)
                            .setCancelable(cancelable)
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