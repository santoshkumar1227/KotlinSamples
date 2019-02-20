package com.launchship.www.mvvm.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.model.User
import com.launchship.www.mvvm.util.Commons
import com.launchship.www.mvvm.util.Extensions.showToast
import com.launchship.www.mvvm.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewsInitialize()
    }

    private fun viewsInitialize() {
        etLogin.setOnClickListener {
            val user = User(Commons.readFromEditText(etUserName), Commons.readFromEditText(etPassword))
            val status = loginViewModel.checkLoginCredentials(user, applicationContext)
            if (!status!!) {
                showToast("Invalid Login")
            } else {
                showToast("Success")
                saveLoginUserInfo(user)
                startActivity(Intent(applicationContext, HomePageActivity::class.java))
            }
        }
    }

    private fun saveLoginUserInfo(user: User) {
        loginViewModel.saveLoginUsername(user, applicationContext)
    }
}
