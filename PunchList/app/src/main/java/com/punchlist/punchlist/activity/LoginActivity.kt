package com.punchlist.punchlist.activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.widget.EditText
import com.punchlist.punchlist.R
import com.punchlist.punchlist.fragment.ForgotPasswordDialog


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun forgotPassword(view: View) {
        /*val taskEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.forgotPassword))
                .setMessage(getString(R.string.passwordSentToRegisterEmail))
                .setView(taskEditText)
                .setPositiveButton(getString(R.string.submit), DialogInterface.OnClickListener { dialog, which ->
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
        dialog.show()*/
        val dFragment = ForgotPasswordDialog()
        // Show DialogFragment
        dFragment.show(supportFragmentManager, "Dialog Fragment")
    }

    fun btnContinue(view: View) {
        startActivity(Intent(applicationContext, AdminUserHomePageActivity::class.java))
    }

}
