package com.venkatesh.schoolmanagement.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseUser
import com.venkatesh.schoolmanagement.R

class LoginActivity : AppCompatActivity() {
    var loginType: String = ""
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        mAuth = FirebaseAuth.getInstance()

        title = getString(R.string.btn_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(Constants.loginType)) {
            when (intent.getStringExtra(Constants.loginType)) {
                Constants.admin -> {
                    title = getString(R.string.admin_login)
                }

                Constants.studnet -> {
                    title = getString(R.string.student_login)
                }

                Constants.teacher -> {
                    title = getString(R.string.teacher_login)
                }
            }
        }

        btnLogin.setOnClickListener { validateLogin() }
    }

/*
    fun loginClick(view: View) {
        if (validateLogin()) {
*/
/*
            mAuth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        callDashboardScreen(user)
                    } else {
                        Commons.showAlertDialog(
                            context = this@LoginActivity,
                            message = getString(R.string.email_not_exist)
                        )
                    }
                })
*//*

        }
    }
*/

    private fun callDashboardScreen(user: FirebaseUser?) {
        when (intent.getStringExtra(Constants.loginType)) {
            Constants.admin -> {
                title = getString(R.string.admin_login)
            }

            Constants.studnet -> {
                title = getString(R.string.student_login)
            }

            Constants.teacher -> {
                title = getString(R.string.teacher_login)
            }
        }
    }

    private fun validateLogin(): Boolean {
        return when {
            etEmail.text.isEmpty() -> {
                Commons.showAlertDialog(context = this@LoginActivity, message = getString(R.string.valid_email))
                false
            }
            etPassword.text.isEmpty() -> {
                Commons.showAlertDialog(context = this@LoginActivity, message = getString(R.string.valid_password))
                false
            }
            else -> true
        }
    }
}
