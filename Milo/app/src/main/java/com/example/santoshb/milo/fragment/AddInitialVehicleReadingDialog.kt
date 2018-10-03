package com.example.santoshb.milo.fragment

import android.content.Context
import android.content.DialogInterface
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
import com.example.santoshb.milo.activity.VehicleDetailActivity
import com.example.santoshb.milo.util.Commons
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_add_vehicle_initial_reading.*

class AddInitialVehicleReadingDialog : DialogFragment() {

    private var realm: Realm? = null
    private var email = ""
    private var initialReading = ""

    fun newInstance(vehicleNo: String): AddInitialVehicleReadingDialog {
        val frag = AddInitialVehicleReadingDialog()
        val args = Bundle()
        args.putString("vehicleNo", vehicleNo)
        frag.arguments = args
        return frag
    }

    interface VehicleReading {
        fun addReading(vehicleReading: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    //    isCancelable = false
        val view = inflater.inflate(R.layout.fragment_add_vehicle_initial_reading, container)
        val btnAddVehicleReading = view.findViewById<Button>(R.id.btnAddVehicleReading)
        btnAddVehicleReading.setOnClickListener {
            if (validateVehicleReading()) {
                (activity as VehicleReading).addReading(initialReading)
                dismiss()
            }
        }

        realm = (activity as VehicleDetailActivity).realm
        email = (activity as VehicleDetailActivity).email
        return view
    }

    private fun validateVehicleReading(): Boolean {
        initialReading = Commons.readFromEditText(etVehicleReading)
        if (TextUtils.isEmpty(initialReading)) {
            Commons.showValidationAlertDialog(
                    activity as Context,
                    activity!!.resources.getString(R.string.enter_initial_reading)
            )
            return false
        }
        return true
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        activity?.finish()
    }
}


