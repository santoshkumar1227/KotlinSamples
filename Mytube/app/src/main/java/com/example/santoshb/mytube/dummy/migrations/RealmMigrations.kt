package com.example.santoshb.mytube.dummy.migrations

import com.example.santoshb.mytube.dummy.Commons
import io.realm.DynamicRealm
import io.realm.RealmMigration


class RealmMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        if (oldVersion === 1L) {
            val userSchema = schema.get("Video")
            userSchema!!.addField(Commons.language, String::class.javaPrimitiveType)
        }
    }
}