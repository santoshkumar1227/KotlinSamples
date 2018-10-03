package com.example.santoshb.milo.database.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class User : RealmModel {
    var id: String =""
    var name: String = ""
    var phone: String = ""
    var email: String = ""
    var passWord: String = ""
}
