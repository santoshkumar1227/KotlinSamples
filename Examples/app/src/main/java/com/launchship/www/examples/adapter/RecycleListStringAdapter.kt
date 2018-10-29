package com.launchship.www.examples.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import com.launchship.www.examples.MainSubActivity
import com.launchship.www.examples.R
import com.launchship.www.examples.model.Doc

class RecycleListStringAdapter(private val context: Context, private var example: List<String>) :
        RecyclerView.Adapter<RecycleListStringAdapter.CustomViewHolder>() {

    var mStringFilterList: List<String>? = null
    private var valueFilter: ValueFilter? = null

    init {
        mStringFilterList = example
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_main, p0, false))
    }

    override fun getItemCount(): Int = example!!.size

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        example[p1]?.let { it ->
            p0.textView.text = it
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    open inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val filterList: ArrayList<String> = ArrayList()
                for (i in 0 until mStringFilterList!!.size) {
                    if (mStringFilterList?.get(i)?.toUpperCase()?.contains(constraint.toString().toUpperCase())!!) {
                        filterList.add(mStringFilterList!!.get(i))
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = mStringFilterList?.size!!
                results.values = mStringFilterList
            }
            return results

        }

        override fun publishResults(constraint: CharSequence,
                                    results: FilterResults) {
            example = results.values as List<String>
            notifyDataSetChanged()
        }

    }
}