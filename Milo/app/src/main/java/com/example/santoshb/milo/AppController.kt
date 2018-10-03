package com.example.santoshb.milo

import android.app.Application
import io.realm.Realm

class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}