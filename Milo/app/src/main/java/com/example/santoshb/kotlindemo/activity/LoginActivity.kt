package com.example.santoshb.kotlindemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.database.model.User
import com.example.santoshb.kotlindemo.util.Commons
import com.example.santoshb.kotlindemo.util.CustomSharedPreferences
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import io.realm.kotlin.where
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        realm = Realm.getDefaultInstance()
    }

    fun onLoginPress(view: View) {
        if (doValidateAllFields()) {
            val userName = Commons.readFromEditText(inputUserName)
            val passWord = Commons.readFromEditText(inputPassword)
            if (isUserExists(userName, passWord)) {
                startActivity(
                        Intent(this, ListOfVehiclesActivity::class.java))
                finish()
            } else {
                Commons.showValidationAlertDialog(this, resources.getString(R.string.UsernameOrPasswordNotCorrect))
            }
        }
    }

    private fun doValidateAllFields(): Boolean {
        if (Commons.isEmptyEditText(inputUserName) || Commons.isEmptyEditText(inputPassword)) {
            Commons.showValidationAlertDialog(this, resources.getString(R.string.UsernameOrPasswordMissing))
            return false
        }
        return true
    }

    fun registration(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun isUserExists(email: String, passWord: String): Boolean {
        val results = realm.where<User>()
                .contains("passWord", passWord)
                .contains("email", email)
                .findAll()

        if (results.size == 0) {
            return false
        } else {
            val user: User? = results[0]
            val jsonObject = JSONObject()
            user?.let {
                jsonObject.put("name", user.name)
                jsonObject.put("email", user.email)
                jsonObject.put("phone", user.phone)
                CustomSharedPreferences.putString(
                        this, "profile", jsonObject.toString()
                )

                CustomSharedPreferences.putString(this, "email", user.email)
            }
        }
        return true
    }
}

