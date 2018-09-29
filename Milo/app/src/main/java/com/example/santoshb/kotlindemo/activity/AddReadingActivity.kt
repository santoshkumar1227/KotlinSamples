package com.example.santoshb.kotlindemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.`interface`.BooleanCallback
import com.example.santoshb.kotlindemo.database.model.VehicleMiloHistory
import com.example.santoshb.kotlindemo.database.model.Vehicles
import com.example.santoshb.kotlindemo.util.*
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_reading.*


class AddReadingActivity : AppCompatActivity() {
    private var vehicleNo: String = ""
    private var email: String = ""
    private var lastReading: Long = 0
    private var presentReading: String = ""
    private var oilPrice: String = ""
    private var oilQuantity: String = ""
    private var amountPaid: String = ""
    private var oilQuantityTrans = true
    private lateinit var realm: Realm
    private var isNewReading = true
    private var vehicleMiloHistory: VehicleMiloHistory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reading)
        realm = Realm.getDefaultInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("vehicle"))
            vehicleNo = intent.getStringExtra("vehicle")
        if (intent.hasExtra("email"))
            email = intent.getStringExtra("email")
        if (intent.hasExtra("lastReading"))
            lastReading = intent.getLongExtra("lastReading", 0L)

        if (intent.hasExtra("logdata")) {
            vehicleMiloHistory = intent.getBundleExtra("logdata").getSerializable("logdata") as VehicleMiloHistory
            isNewReading = false
            updateUiForEdit()
        }

        etPrevReading.setText(lastReading.toString())

        radioButtonListeners()
    }

    private fun updateUiForEdit() {
        vehicleMiloHistory?.let {
            etPresentReading.setText(it.currentReading.toString())
            lastReading = it.previousReading
            if (it.amountPaid == 0) {
                radioOil.isChecked = true
                etOil.setText(it.noOfLiters.toString())
                linearOilAmount.visibility = View.GONE
                inputOilQuantity.visibility = View.VISIBLE
            } else {
                radioAmount.isChecked = true
                etAmountPaid.setText(it.amountPaid.toString())
                etPrice.setText(it.price.toString())
                linearOilAmount.visibility = View.VISIBLE
                inputOilQuantity.visibility = View.GONE
            }
        }
    }


    private fun radioButtonListeners() {
        radioAmount.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                oilQuantityTrans = false
                linearOilAmount.visibility = View.VISIBLE
                inputOilQuantity.visibility = View.GONE
            } else {
                oilQuantityTrans = true
                linearOilAmount.visibility = View.GONE
                inputOilQuantity.visibility = View.VISIBLE
            }
        }

        radioOil.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                oilQuantityTrans = true
                linearOilAmount.visibility = View.GONE
                inputOilQuantity.visibility = View.VISIBLE
            } else {
                oilQuantityTrans = false
                linearOilAmount.visibility = View.VISIBLE
                inputOilQuantity.visibility = View.GONE
            }
        }

    }

    fun saveReading(view: View) {
        if (validateAll()) {
            try {
                if (!oilQuantityTrans) {
                    oilQuantity = (amountPaid.toFloat() / oilPrice.toFloat()).toString()
                }

                val diffReading: Long = presentReading.toLong() - lastReading
                val mileage: Float = diffReading / oilQuantity.toFloat()
                try {
                    if (isNewReading) {
                        realm.let { it ->
                            it.executeTransactionAsync({ realm ->
                                // Add a person
                                val oneHistory = realm.createObject<VehicleMiloHistory>()
                                oneHistory.id = (System.currentTimeMillis() / 1000).toString()
                                oneHistory.email = email
                                oneHistory.vehicleNo = vehicleNo
                                oneHistory.currentReading = presentReading.toLong()
                                oneHistory.previousReading = lastReading
                                if (oilQuantityTrans) {
                                    oneHistory.noOfLiters = oilQuantity.toFloat()
                                } else {
                                    oneHistory.price = oilPrice.toFloat()
                                    oneHistory.amountPaid = amountPaid.toInt()
                                }

                                oneHistory.dateAdded = "".getDate()
                                oneHistory.Mileage = mileage

                            }, {
                                dbResult(true)
                            }, {
                                dbResult(false)
                            })
                        }
                    } else {
                        realm.let { it ->
                            val results = it.where<VehicleMiloHistory>()
                                    .contains("id", vehicleMiloHistory?.id)
                                    .findAll()

                            if (results.size != 0) {
                                it.executeTransaction {
                                    val oneHistory = results[0]
                                    oneHistory?.currentReading = presentReading.toLong()
                                    if (oilQuantityTrans) {
                                        oneHistory?.noOfLiters = oilQuantity.toFloat()
                                    } else {
                                        oneHistory?.price = oilPrice.toFloat()
                                        oneHistory?.amountPaid = amountPaid.toInt()
                                    }

                                    oneHistory?.dateAdded = "".getDate()
                                    oneHistory?.Mileage = mileage
                                }
                            }
                        }
                    }
                    dbResult(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (ex: java.lang.Exception) {
                Commons.showValidationAlertDialog(this, resources.getString(R.string.valuesNotProper))
            }
        }
    }

    private fun dbResult(b: Boolean) {
        if (b) {
            realm.let { it ->
                val results = it.where<Vehicles>()
                        .contains("email", email)
                        .findAll()

                it.executeTransaction {
                    for (item in results) {
                        if (item.vehicleNo.stringEquals(vehicleNo)) {
                            item.latestReading = presentReading.toLong()
                        }
                    }
                }
            }

            var successMsg = ""

            successMsg = if (isNewReading)
                resources.getString(R.string.successfullyAdded)
            else
                resources.getString(R.string.successfullyUpdated)


            Commons.showValidationAlertDialog(this, successMsg,
                    object : BooleanCallback {
                        override fun booleanCallback(boolean: Boolean) {
                            if (boolean) {
                                val prevIntent = Intent()
                                prevIntent.putExtra("lastReading", presentReading.toLong())
                                setResult(100, prevIntent)
                                finish()
                            }
                        }
                    })
        } else {
            Commons.showValidationAlertDialog(this, resources.getString(R.string.failedToAdd))
        }
    }

    private fun validateAll(): Boolean {
        presentReading = Commons.readFromEditText(etPresentReading)
        oilPrice = Commons.readFromEditText(etPrice)
        oilQuantity = Commons.readFromEditText(etOil)
        amountPaid = Commons.readFromEditText(etAmountPaid)
        var validateMessage = ""

        if (TextUtils.isEmpty(presentReading) || presentReading.stringEquals("0")) {
            validateMessage = resources.getString(R.string.enter_current_reading)
        } else if (presentReading.toLong() <= lastReading) {
            validateMessage = resources.getString(R.string.enter_valid_readings)
        } else {
            if (oilQuantityTrans) {
                if (TextUtils.isEmpty(oilQuantity)) {
                    validateMessage = resources.getString(R.string.enter_oil_quantity)
                }
            } else {
                if (TextUtils.isEmpty(amountPaid) || amountPaid.toInt() == 0) {
                    validateMessage = resources.getString(R.string.enter_amount_paid)
                } else if (TextUtils.isEmpty(oilPrice) || oilPrice.toInt() == 0) {
                    validateMessage = resources.getString(R.string.enter_oil_price)
                }
            }
        }

        if (!validateMessage.stringEquals("")) {
            Commons.showValidationAlertDialog(this, validateMessage)
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }


}
