package com.punchlist.punchlist.utils

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


object Commons {
    fun showValidationDialog(context: Context, message: String) {
        val builder1 = AlertDialog.Builder(context)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        /*builder1.setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
*/
        val alert11 = builder1.create()
        alert11.show()
    }
}