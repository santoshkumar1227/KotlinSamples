package com.example.santoshb.kotlindemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.santoshb.kotlindemo.R
import io.realm.Realm

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val realm: Realm = Realm.getDefaultInstance()
        /*realm.executeTransaction {
            realm.deleteAll()
        }*/
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}