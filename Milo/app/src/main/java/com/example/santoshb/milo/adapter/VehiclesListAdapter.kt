package com.example.santoshb.milo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.santoshb.milo.R

class VehiclesListAdapter(context: Context, resource: Int, list: ArrayList<String>) :
        ArrayAdapter<String>(context, resource, list) {

    var resource: Int = resource
    private var list: ArrayList<String> = list
    private var vi: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val holder: ViewHolder
        val retView: View

        if (convertView == null) {
            retView = vi.inflate(resource, null) //error in this line
            holder = ViewHolder()
            holder.tvVehicleNumber = retView.findViewById(R.id.tvVehicleNumber) as TextView?
            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.tvVehicleNumber?.text = list[position]

        return retView
    }

    internal class ViewHolder {
        var tvVehicleNumber: TextView? = null
    }

}