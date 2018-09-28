package com.example.santoshb.kotlindemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.`interface`.BooleanCallback
import com.example.santoshb.kotlindemo.database.model.User
import com.example.santoshb.kotlindemo.util.Commons
import com.example.santoshb.kotlindemo.util.CustomSharedPreferences
import com.example.santoshb.kotlindemo.util.stringEquals
import io.realm.Realm
import io.realm.kotlin.where
import org.json.JSONObject

class SplashActivity : Activity() {
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        realm = Realm.getDefaultInstance()
        Handler().postDelayed({
            if (CustomSharedPreferences.getString(this, "firstTime")?.stringEquals("")!!) {
                Commons.showValidationAlertDialog(this,
                        resources.getString(R.string.info_message),
                        object : BooleanCallback {
                            override fun booleanCallback(boolean: Boolean) {
                                if (boolean) {
                                    CustomSharedPreferences.putString(this@SplashActivity, "firstTime", "no")
                                    isUserExists()
                                }
                            }
                        }, false)
            } else
                isUserExists()
        }, 3000)
    }

    private fun isUserExists() {
        val results = realm.where<User>().findAll()

        if (results.size == 0) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
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
                startActivity(Intent(this, ListOfVehiclesActivity::class.java))
                finish()
            }
        }
    }

}