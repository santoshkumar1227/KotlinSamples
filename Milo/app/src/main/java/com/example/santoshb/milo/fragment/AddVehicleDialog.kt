package com.example.santoshb.milo.fragment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.example.santoshb.milo.R
import com.example.santoshb.milo.`interface`.BooleanCallback
import com.example.santoshb.milo.activity.ListOfVehiclesActivity
import com.example.santoshb.milo.database.model.VehicleMiloHistory
import com.example.santoshb.milo.database.model.Vehicles
import com.example.santoshb.milo.util.Commons
import com.example.santoshb.milo.util.stringEquals
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_vehicle_detail.*
import kotlinx.android.synthetic.main.fragment_add_vehicle.*

class AddVehicleDialog : DialogFragment() {

    private var vehicleNoFromList: String = ""
    private var vehicleNo: String = ""
    private var isCreated: Boolean = true
    private var realm: Realm? = null
    private var email = ""

    fun newInstance(vehicleNo: String): AddVehicleDialog {
        val frag = AddVehicleDialog()
        val args = Bundle()
        args.putString("vehicleNo", vehicleNo)
        frag.arguments = args
        return frag
    }

    interface AddVehicle {
        fun addVehicleNo(vehicle: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey("vehicleNo")) {
                vehicleNoFromList = it.getString("vehicleNo")
                isCreated = false
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val view = inflater.inflate(R.layout.fragment_add_vehicle, container)
        val btnAddVehicle = view.findViewById<Button>(R.id.btnAddVehicle)
        btnAddVehicle.setOnClickListener {
            if (validateVehicle()) {
                if (!isVehicleAddedAlready()) {
                    if (isCreated)
                        insertVehicle(vehicleNo)
                    else {
                        if (canUpdatable()) {
                            updateVehicle(vehicleNo)
                        } else {
                            activity?.let { it1 ->
                                Commons.showValidationAlertDialog(it1,
                                        resources.getString(R.string.tripsAddedAlready))
                            }
                        }
                    }
                    dismiss()
                } else {
                    if (isCreated) {
                        activity?.let { it1 -> Commons.showValidationAlertDialog(it1, resources.getString(R.string.vehicle_added_already)) }
                    }else{
                        dismiss()
                    }
                }
            }
        }

        realm = (activity as ListOfVehiclesActivity).realm
        email = (activity as ListOfVehiclesActivity).email!!
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!isCreated) {
            activity?.let {
                btnAddVehicle.text = it.resources.getString(R.string.edit)
                etVehicle.append(vehicleNoFromList)
            }
        }
    }

    private fun canUpdatable(): Boolean {
        realm?.let { it ->
            val results = it.where<VehicleMiloHistory>()
                    .contains("email", email)
                    .contains("vehicleNo", vehicleNoFromList)
                    .findAll()

            return results.size == 0
        }
        return true
    }

    private fun updateVehicle(vehicleNo: String) {
        realm?.let { it ->
            val results = it.where<Vehicles>()
                    .contains("email", email)
                    .findAll()

            it.executeTransaction {
                for (item in results) {
                    if (vehicleNoFromList.stringEquals(item.vehicleNo)) {
                        item.vehicleNo = vehicleNo
                    }
                }
            }
            dbResult(true, "edit")
        }

    }

    private fun isVehicleAddedAlready(): Boolean {
        realm?.let { it ->
            val results = it.where<Vehicles>()
                    .contains("email", email)
                    .contains("vehicleNo", vehicleNo)
                    .findAll()
            if (results.size != 0)
                return true
        }

        return false
    }

    private fun insertVehicle(vehicleNo: String) {
        try {

            /*realm?.let { it ->
                it.executeTransactionAsync({ realm ->
                    // Add a person
                    val person = realm.createObject<Vehicles>()
                    person.email = email
                    person.vehicleNo = vehicleNo
                    person.id = (System.currentTimeMillis() / 1000).toString()
                }, {
                    dbResult(true)
                }, {
                    dbResult(false)
                })
            }*/
            realm?.let {
                it.executeTransaction {
                    // Add a person
                    val person = it.createObject<Vehicles>()
                    person.email = email
                    person.vehicleNo = vehicleNo
                    person.id = (System.currentTimeMillis() / 1000).toString()
                }
            }
            dbResult(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun dbResult(boolean: Boolean, type: String = "create") {
        activity?.let {
            if (boolean) {
                val successMsg = if (type.stringEquals("edit")) {
                    resources.getString(R.string.vehicleUpdateSuccess)
                } else {
                    resources.getString(R.string.vehicleInsertSuccess)
                }

                Commons.showValidationAlertDialog(it, successMsg, object : BooleanCallback {
                    override fun booleanCallback(boolean: Boolean) {
                        (it as AddVehicle).addVehicleNo(vehicleNo)
                        dismiss()
                    }
                })
            } else
                Commons.showValidationAlertDialog(it, resources.getString(R.string.vehicleInsertFailed))
        }
    }

    private fun validateVehicle(): Boolean {
        vehicleNo = Commons.readFromEditText(etVehicle)
        if (TextUtils.isEmpty(vehicleNo)) {
            Commons.showValidationAlertDialog(
                    activity as Context,
                    activity!!.resources.getString(R.string.add_vehicle_no_miss)
            )
            return false
        }
        return true
    }
}


