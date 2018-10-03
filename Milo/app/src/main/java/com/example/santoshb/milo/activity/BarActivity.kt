package com.example.santoshb.milo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.santoshb.milo.R
import com.example.santoshb.milo.database.model.VehicleMiloHistory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_reading.*
import kotlinx.android.synthetic.main.activity_bar.*

class BarActivity : AppCompatActivity() {
    private var vehicleNo: String = ""
    var email: String = ""
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar)
        addAdds()
        realm = Realm.getDefaultInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("vehicle"))
            vehicleNo = intent.getStringExtra("vehicle")
        if (intent.hasExtra("email"))
            email = intent.getStringExtra("email")

        getReadingsList()

    }

    private fun getReadingsList() {
        val results = realm.where<VehicleMiloHistory>()
                .contains("vehicleNo", vehicleNo)
                .contains("email", email)
                .findAll()
        val list = ArrayList<String>()
        val barDataList = ArrayList<Int>()
        results?.let {
            if (results.size != 0) {
                for (item in results) {
                    list.add(item.Mileage.toInt().toString())
                    barDataList.add(item.Mileage.toInt())
                }
            }
        }
        barView.setBottomTextList(list)
        barView.setDataList(barDataList, 100)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
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
        adViewGraph.loadAd(adRequest)
    }

}
