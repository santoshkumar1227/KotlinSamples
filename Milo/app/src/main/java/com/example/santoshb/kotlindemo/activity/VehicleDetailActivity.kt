package com.example.santoshb.kotlindemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.adapter.VehicleMiloHistoryAdapter
import com.example.santoshb.kotlindemo.database.model.VehicleMiloHistory
import com.example.santoshb.kotlindemo.database.model.Vehicles
import com.example.santoshb.kotlindemo.fragment.AddInitialVehicleReadingDialog
import com.example.santoshb.kotlindemo.util.stringEquals
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_vehicle_detail.*


class VehicleDetailActivity : AppCompatActivity(), AddInitialVehicleReadingDialog.VehicleReading {

    override fun addReading(vehicleReading: String) {
        lastReading = vehicleReading.toLong()
        updateLastReading(lastReading)
    }

    private var vehicleNo: String = ""
    var email: String = ""
    private var lastReading: Long = 0
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)
        realm = Realm.getDefaultInstance()

        if (intent.hasExtra("vehicle"))
            vehicleNo = intent.getStringExtra("vehicle")
        if (intent.hasExtra("email"))
            email = intent.getStringExtra("email")


        supportActionBar?.let {
            it.title = vehicleNo
            it.setDisplayHomeAsUpEnabled(true)
        }

        checkLastReading()

        getReadingsList()

    }

    private fun getReadingsList() {
        val results = realm.where<VehicleMiloHistory>()
                .contains("vehicleNo", vehicleNo)
                .contains("email", email)
                .findAll()

        results?.let {
            if (results.size == 0) {
                noMillageAdded.visibility = View.VISIBLE
            } else {
                val list = ArrayList<VehicleMiloHistory>()
                for (item in results) {
                    val vehicleMiloHistory = VehicleMiloHistory()
                    vehicleMiloHistory.id = item.id
                    vehicleMiloHistory.email = item.email
                    vehicleMiloHistory.vehicleNo = item.vehicleNo
                    vehicleMiloHistory.noOfLiters = item.noOfLiters
                    vehicleMiloHistory.dateAdded = item.dateAdded
                    vehicleMiloHistory.currentReading = item.currentReading
                    vehicleMiloHistory.previousReading = item.previousReading
                    vehicleMiloHistory.millage = item.millage
                    vehicleMiloHistory.price = item.price
                    vehicleMiloHistory.amountPaid = item.amountPaid
                    list.add(vehicleMiloHistory)
                }
                recycleVehiclesListHistory.layoutManager = LinearLayoutManager(this)
                val vehicleMiloHistoryAdapter = VehicleMiloHistoryAdapter(list, this)
                recycleVehiclesListHistory.adapter = vehicleMiloHistoryAdapter
                noMillageAdded.visibility = View.GONE
            }
        }
    }

    private fun checkLastReading() {
        val results = realm.where<Vehicles>()
                .contains("vehicleNo", vehicleNo)
                .contains("email", email)
                .findAll()

        results?.let {
            if (results.size != 0) {
                lastReading = results[0]!!.latestReading
                if (lastReading == 0L) {
                    supportFragmentManager.beginTransaction().add(AddInitialVehicleReadingDialog(), "reading").commit()
                }
                updateTvLastReading()
            } else {
                finish()
            }
        }
    }

    private fun updateLastReading(vehicleReading: Long) {
        realm.let { it ->
            val results = it.where<Vehicles>()
                    .contains("email", email)
                    .contains("vehicleNo", vehicleNo)
                    .findAll()

            it.executeTransaction {
                for (item in results) {
                    if (item.vehicleNo.stringEquals(vehicleNo)) {
                        item.latestReading = vehicleReading
                    }
                }
            }
        }
        updateTvLastReading()
    }

    private fun updateTvLastReading() {
        tvLastReading.text = resources.getString(R.string.latest_reading) +
                " :: " + lastReading.toString() + " " + resources.getString(R.string.km)
    }

    fun addMileage(view: View) {
        val detailIntent = Intent(this, AddReadingActivity::class.java)
        detailIntent.putExtra("vehicle", vehicleNo)
        detailIntent.putExtra("email", email)
        detailIntent.putExtra("lastReading", lastReading)
        startActivityForResult(detailIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == 100) {
            data?.let {
                if (it.hasExtra("lastReading")) {
                    lastReading = it.getLongExtra("lastReading", lastReading)
                    updateTvLastReading()
                    getReadingsList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vehicle_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.graphView -> {
                val detailIntent = Intent(this, BarActivity::class.java)
                detailIntent.putExtra("vehicle", vehicleNo)
                detailIntent.putExtra("email", email)
                startActivity(detailIntent)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
