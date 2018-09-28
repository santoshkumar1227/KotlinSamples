package com.example.santoshb.kotlindemo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.database.model.VehicleMiloHistory
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_bar.*
import kotlinx.android.synthetic.main.activity_vehicle_detail.*

class BarActivity : AppCompatActivity() {
    private var vehicleNo: String = ""
    var email: String = ""
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar)
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
}
