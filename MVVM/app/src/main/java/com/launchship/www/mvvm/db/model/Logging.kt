package com.launchship.www.mvvm.db.model

import android.arch.persistence.room.Entity
import java.io.Serializable
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey


@Entity
class Logging : Serializable {
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0

    @ColumnInfo(name = "acno")
    private var accNo: String? = null

    @ColumnInfo(name = "transactionType")
    private var transactionType: String? = null

    @ColumnInfo(name = "transactionAmount")
    private var transactionAmount: Int? = null

    @ColumnInfo(name = "transactionDate")
    private var transactionDate: String? = null


    fun getAccNo(): String {
        return accNo.toString()
    }

    fun setAccNo(accNo: String) {
        this.accNo = accNo
    }

    fun getTransactionType(): String {
        return transactionType.toString()
    }

    fun setTransactionType(transactionType: String) {
        this.transactionType = transactionType
    }

    fun getTransactionAmount(): Int? {
        return transactionAmount
    }

    fun setTransactionAmount(transactionAmount: Int) {
        this.transactionAmount = transactionAmount
    }

    fun getTransactionDate(): String {
        return transactionDate.toString()
    }

    fun setTransactionDate(transactionDate: String) {
        this.transactionDate = transactionDate
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

}