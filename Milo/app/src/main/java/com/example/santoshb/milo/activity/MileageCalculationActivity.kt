package com.example.santoshb.milo.activity

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.example.santoshb.milo.R
import com.example.santoshb.milo.`interface`.BooleanCallback
import com.example.santoshb.milo.util.Commons
import com.example.santoshb.milo.util.stringEquals
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_add_reading.*
import kotlinx.android.synthetic.main.activity_list_of_vehicles.*
import java.lang.Exception
import android.widget.Toast
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES
import android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO



class MileageCalculationActivity : AppCompatActivity() {
    private var lastReading: String = ""
    private var presentReading: String = ""
    private var oilPrice: String = ""
    private var oilQuantity: String = ""
    private var amountPaid: String = ""
    private var oilQuantityTrans = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reading)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addAdds()
        etPrevReading.isEnabled = true
        btnSave.text = resources.getString(R.string.calculate)
        radioButtonListeners()
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
                val diffReading: Long = presentReading.toLong() - lastReading.toLong()
                val mileage: Float = diffReading / oilQuantity.toFloat()

                Commons.showValidationAlertDialog(this,
                        resources.getString(R.string.mileageReceived) + " :: " + mileage + " " + resources.getString(R.string.km_lt),
                        object : BooleanCallback {
                            override fun booleanCallback(boolean: Boolean) {
                                if (boolean) {
                                    startActivity(Intent(this@MileageCalculationActivity,
                                            MileageCalculationActivity::class.java))
                                    finish()
                                }
                            }
                        }, false)
            } catch (ex: Exception) {
                Commons.showValidationAlertDialog(this, resources.getString(R.string.valuesNotProper))
            }
        }
    }

    private fun addAdds() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, resources.getString(R.string.banner_ad_app_id))


        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()

        // Start loading the ad in the background.
        adViewAddReading.loadAd(adRequest)
    }

    private fun validateAll(): Boolean {
        presentReading = Commons.readFromEditText(etPresentReading)
        oilPrice = Commons.readFromEditText(etPrice)
        oilQuantity = Commons.readFromEditText(etOil)
        amountPaid = Commons.readFromEditText(etAmountPaid)
        lastReading = Commons.readFromEditText(etPrevReading)
        var validateMessage = ""

        if (TextUtils.isEmpty(presentReading) || presentReading.stringEquals("0")) {
            validateMessage = resources.getString(R.string.enter_current_reading)
        } else if (TextUtils.isEmpty(lastReading) || lastReading.stringEquals("0")) {
            validateMessage = resources.getString(R.string.enter_current_reading)
        } else if (presentReading.toLong() <= lastReading.toLong()) {
            validateMessage = resources.getString(R.string.enter_valid_readings)
        } else {
            if (oilQuantityTrans) {
                if (TextUtils.isEmpty(oilQuantity)) {
                    validateMessage = resources.getString(R.string.enter_Fuel_quantity)
                }
            } else {
                if (TextUtils.isEmpty(amountPaid) || amountPaid.toFloat() == 0F) {
                    validateMessage = resources.getString(R.string.enter_amount_paid)
                } else if (TextUtils.isEmpty(oilPrice) || oilPrice.toFloat() == 0F) {
                    validateMessage = resources.getString(R.string.enter_Fuel_price)
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show()
        } else if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show()
        }
    }

}
