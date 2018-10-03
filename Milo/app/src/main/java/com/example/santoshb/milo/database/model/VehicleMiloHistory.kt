package com.example.santoshb.milo.database.model

import io.realm.RealmModel
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class VehicleMiloHistory : RealmModel,Serializable{
    var id: String = ""
    var email: String = ""
    var vehicleNo: String = ""
    var dateAdded: String = ""
    var currentReading: Long = 0
    var previousReading: Long = 0
    var noOfLiters: Float = 0.0f
    var Mileage: Float = 0.0f
    var price: Float = 0.0f
    var amountPaid: Int = 0
}