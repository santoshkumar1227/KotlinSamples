package com.example.santoshb.kotlindemo.database.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class Vehicles : RealmModel {
    var id: String = ""
    var email: String = ""
    var vehicleNo: String = ""
    var latestReading: Long = 0
}
