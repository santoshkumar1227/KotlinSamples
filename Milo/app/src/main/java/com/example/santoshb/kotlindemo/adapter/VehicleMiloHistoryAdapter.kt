package com.example.santoshb.kotlindemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.santoshb.kotlindemo.R
import com.example.santoshb.kotlindemo.database.model.VehicleMiloHistory
import kotlinx.android.synthetic.main.vehicle_list_history_item.view.*

class VehicleMiloHistoryAdapter(private val items: ArrayList<VehicleMiloHistory>, val context: Context) :
        RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_list_history_item, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val vehicleMiloHistory: VehicleMiloHistory = items[p1]
        p0.tvReading.text = context.resources.getString(R.string.log) + " #" + (p1 + 1)
        p0.tvDateAdded.text = vehicleMiloHistory.dateAdded
        p0.tvMillage.text = context.resources.getString(R.string.millage) + " :: " +
                vehicleMiloHistory.millage.toString() + " " + context.resources.getString(R.string.km_hr)
        p0.tvBeforeReading.text = context.resources.getString(R.string.previous_reading) + " :: " +
                vehicleMiloHistory.previousReading.toString()+ " " + context.resources.getString(R.string.km)
        p0.tvAfterReading.text = context.resources.getString(R.string.reading) + " :: " +
                vehicleMiloHistory.currentReading.toString()+ " " + context.resources.getString(R.string.km)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvDateAdded: TextView = view.tvDateAdded
    val tvMillage: TextView = view.tvMillage
    val tvBeforeReading: TextView = view.tvBeforeReading
    val tvReading: TextView = view.tvReading
    val tvAfterReading: TextView = view.tvAfterReading
}