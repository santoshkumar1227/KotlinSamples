package com.example.santoshb.milo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.santoshb.milo.R
import com.example.santoshb.milo.database.model.User
import com.example.santoshb.milo.util.Commons
import com.example.santoshb.milo.util.CustomSharedPreferences
import com.example.santoshb.milo.util.isEmail
import com.example.santoshb.milo.util.stringEquals
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_register.*


class ProfileActivity : AppCompatActivity() {

    private var mName: String = ""
    private var mEmail: String = ""
    private var mPhone: String = ""
    private var mPassword: String = ""
    private var mConfPassword: String = ""
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Open the realm for the UI thread.
        realm = Realm.getDefaultInstance()
        readUserProfile()
    }

    private fun readAllViews() {
        mName = Commons.readFromEditText(etName)
        mEmail = Commons.readFromEditText(etEmail)
        mPhone = Commons.readFromEditText(etPhone)
        mPassword = Commons.readFromEditText(etPassword)
        mConfPassword = Commons.readFromEditText(etConfPassword)
    }

    private fun doValidateAll(): Boolean {
        var validationMessage = ""
        if (TextUtils.isEmpty(mName)) {
            validationMessage = resources.getString(R.string.enterName)
        } else if (TextUtils.isEmpty(mEmail)) {
            validationMessage = resources.getString(R.string.enterEmail)
        } else if (!mEmail.isEmail()) {
            validationMessage = resources.getString(R.string.enterValidEmail)
        } else if (TextUtils.isEmpty(mPhone)) {
            validationMessage = resources.getString(R.string.enterPhone)
        } else if (TextUtils.isEmpty(mPassword)) {
            validationMessage = resources.getString(R.string.enterPassword)
        } else if (TextUtils.isEmpty(mConfPassword)) {
            validationMessage = resources.getString(R.string.enterConfPassword)
        } else if (!mPassword.stringEquals(mConfPassword)) {
            validationMessage = resources.getString(R.string.passwordNotMatch)
        }

        return if (validationMessage.stringEquals(""))
            true
        else {
            Commons.showValidationAlertDialog(this, validationMessage)
            false
        }
    }

    private fun readUserProfile() {
        val email: String? = CustomSharedPreferences.getString(this, "email")
        val results = realm.where<User>()
                .contains("email", email)
                .findAll()

        if (results.size != 0) {
            val user: User? = results[0]
            user?.let {
                etName.setText(user.name)
                etPhone.setText(user.phone)
                etEmail.setText(user.email)
            }
        }
    }
}
