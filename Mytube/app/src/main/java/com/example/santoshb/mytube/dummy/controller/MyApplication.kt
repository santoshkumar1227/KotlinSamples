package com.example.santoshb.mytube.dummy.controller

import android.app.Application
import com.example.santoshb.mytube.dummy.migrations.RealmMigrations
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration =
                RealmConfiguration.Builder().name("mytube.realm").schemaVersion(2).migration(RealmMigrations()).build()

        Realm.setDefaultConfiguration(configuration)
        Realm.getInstance(configuration)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate()
    }
}