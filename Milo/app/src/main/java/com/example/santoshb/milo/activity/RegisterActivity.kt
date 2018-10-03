package com.example.santoshb.milo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.santoshb.milo.R
import com.example.santoshb.milo.`interface`.BooleanCallback
import com.example.santoshb.milo.database.model.User
import com.example.santoshb.milo.util.Commons
import com.example.santoshb.milo.util.CustomSharedPreferences
import com.example.santoshb.milo.util.isEmail
import com.example.santoshb.milo.util.stringEquals
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private var mName: String = ""
    private var mEmail: String = ""
    private var mPhone: String = ""
    private var mPassword: String = ""
    private var mConfPassword: String = ""
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //  supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Open the realm for the UI thread.
        realm = Realm.getDefaultInstance()
    }

    fun register(view: View) {
        readAllViews()
        if (doValidateAll()) {
            insertUserIntoDatabase()
        }
    }

    private fun dbResult(boolean: Boolean) {
        if (boolean) {
            Commons.showValidationAlertDialog(this, resources.getString(R.string.userInsertSuccess),
                    object : BooleanCallback {
                        override fun booleanCallback(boolean: Boolean) {
                            CustomSharedPreferences.putString(this@RegisterActivity, "email", mEmail)
                            startActivity(Intent(this@RegisterActivity, ListOfVehiclesActivity::class.java))
                            finish()
                        }
                    })

        } else
            Commons.showValidationAlertDialog(this, resources.getString(R.string.userInsertFailed))
    }

    private fun insertUserIntoDatabase() {
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        try {
            realm.executeTransactionAsync({ realm ->
                // Add a person
                val person = realm.createObject<User>()
                person.name = mName
                person.email = mEmail
                person.passWord = mPassword
                person.phone = mPhone
                person.id = (System.currentTimeMillis() / 1000).toString()
            }, {
                dbResult(true)
            }, {
                dbResult(false)
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

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
        } /*else if (TextUtils.isEmpty(mPassword)) {
            validationMessage = resources.getString(R.string.enterPassword)
        } else if (TextUtils.isEmpty(mConfPassword)) {
            validationMessage = resources.getString(R.string.enterConfPassword)
        } else if (!mPassword.stringEquals(mConfPassword)) {
            validationMessage = resources.getString(R.string.passwordNotMatch)
        }*/

        return if (validationMessage.stringEquals(""))
            true
        else {
            Commons.showValidationAlertDialog(this, validationMessage)
            false
        }
    }
}
