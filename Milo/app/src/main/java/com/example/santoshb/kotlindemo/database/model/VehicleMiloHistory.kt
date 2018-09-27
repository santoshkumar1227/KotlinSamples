package com.example.santoshb.kotlindemo.database.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class VehicleMiloHistory : RealmModel {
    var id: String = ""
    var email: String = ""
    var vehicleNo: String = ""
    var dateAdded: String = ""
    var currentReading: Long = 0
    var previousReading: Long = 0
    var noOfLiters: Float = 0.0f
    var millage: Float = 0.0f
    var price: Float = 0.0f
    var amountPaid: Int = 0
}