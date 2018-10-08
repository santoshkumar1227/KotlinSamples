package com.example.santoshb.mytube.dummy.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class Video : RealmModel, Serializable {
    var id: String = ""
    var title: String = ""
    var type: String = ""
    var language: String = ""
}
