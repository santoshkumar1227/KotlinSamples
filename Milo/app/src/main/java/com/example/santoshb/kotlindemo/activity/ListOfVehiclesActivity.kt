package com.example.santoshb.kotlindemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.`interface`.BooleanCallback
import com.example.santoshb.kotlindemo.adapter.VehiclesListAdapter
import com.example.santoshb.kotlindemo.database.model.Vehicles
import com.example.santoshb.kotlindemo.fragment.AddVehicleDialog
import com.example.santoshb.kotlindemo.util.Commons
import com.example.santoshb.kotlindemo.util.CustomSharedPreferences
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_list_of_vehicles.*
import android.widget.AdapterView.AdapterContextMenuInfo
import com.example.santoshb.kotlindemo.database.model.VehicleMiloHistory
import com.example.santoshb.kotlindemo.util.stringEquals
import io.realm.kotlin.deleteFromRealm


class ListOfVehiclesActivity : AppCompatActivity(), AddVehicleDialog.AddVehicle {

    private var vehiclesListAdapter: VehiclesListAdapter? = null
    private var listOfVehicles: ArrayList<String> = ArrayList()
    var email: String? = ""
    lateinit var realm: Realm
    override fun addVehicleNo(vehicle: String) {
        loadVehicles()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_vehicles)
        realm = Realm.getDefaultInstance()

        loadVehicles()

        fabAddVehicle.setOnClickListener {
            supportFragmentManager.beginTransaction().add(AddVehicleDialog(), "add").commit()
        }

        registerForContextMenu(recycleVehiclesList)

        recycleVehiclesList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
            val detailIntent = Intent(this, VehicleDetailActivity::class.java)
            detailIntent.putExtra("vehicle", listOfVehicles[position])
            detailIntent.putExtra("email", email)
            startActivity(detailIntent)
        }
    }

    private fun loadVehicles() {
        listOfVehicles()
        loadRecyclerView()
    }


    private fun loadRecyclerView() {
        //listOfVehicles = resources.getStringArray(R.array.vehiclesList)
        vehiclesListAdapter = VehiclesListAdapter(
                this, R.layout.item_card_text_view, listOfVehicles)
        recycleVehiclesList.adapter = vehiclesListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                Commons.showConfirmationAlertDialog(
                        this,
                        resources.getString(R.string.logoutMessage),
                        object : BooleanCallback {
                            override fun booleanCallback(boolean: Boolean) {
                                if (boolean) {
                                    CustomSharedPreferences.putString(this@ListOfVehiclesActivity, "profile")
                                    startActivity(Intent(this@ListOfVehiclesActivity, LoginActivity::class.java))
                                    finish()

                                }
                            }

                        }
                )
            }

            R.id.profile -> {
                startActivity(
                        Intent(this@ListOfVehiclesActivity, ProfileActivity::class.java)
                )
            }

            R.id.mileageSuggestions -> {
                startActivity(
                        Intent(this@ListOfVehiclesActivity, MileageSuggestionsActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listOfVehicles() {
        listOfVehicles.clear()
        email = CustomSharedPreferences.readString(this, "email")
        val results = realm.where<Vehicles>()
                .contains("email", email)
                .findAll()
        if (results.size == 0) {
            noVehicles.visibility = View.VISIBLE
        } else {
            noVehicles.visibility = View.GONE
            for (vehicle in results) {
                listOfVehicles.add(vehicle.vehicleNo)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.add_edit_menu, menu)
        menu?.setHeaderTitle(resources.getString(R.string.select_action))
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item?.menuInfo as AdapterContextMenuInfo
        val index = info.position
        when {
            item.itemId == R.id.edit -> {
                contextItemSelected(index, item.itemId)
            }
            item.itemId == R.id.delete -> {
                contextItemSelected(index, item.itemId)
            }
            else -> return false
        }
        return true
    }

    private fun contextItemSelected(index: Int, itemId: Int) {
        when (itemId) {
            R.id.edit -> {
                val addVehicleDialog: AddVehicleDialog = AddVehicleDialog().newInstance(listOfVehicles[index])
                supportFragmentManager.beginTransaction().add(addVehicleDialog, "add").commit()
            }
            R.id.delete -> {
                Commons.showConfirmationAlertDialog(this,
                        resources.getString(R.string.are_you_sure_want_to_delete),
                        object : BooleanCallback {
                            override fun booleanCallback(boolean: Boolean) {
                                if (boolean) {
                                    val results = realm.where<Vehicles>()
                                            .contains("email", email)
                                            .findAll()

                                    realm.executeTransaction {
                                        val deleteVehicleNo: String = listOfVehicles[index]
                                        for (item in results) {
                                            if (deleteVehicleNo.stringEquals(item.vehicleNo)) {
                                                item.deleteFromRealm()
                                            }
                                        }
                                    }

                                    val resultsLog = realm.where<VehicleMiloHistory>()
                                            .contains("email", email)
                                            .contains("vehicleNo", listOfVehicles[index])
                                            .findAll()

                                    realm.executeTransaction {
                                        resultsLog.deleteAllFromRealm()
                                    }

                                    loadVehicles()
                                }
                            }
                        })
            }
        }
    }
}
