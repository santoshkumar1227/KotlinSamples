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

class RecycleAdapter(private val context: Context, private var example: List<Doc>) :
        RecyclerView.Adapter<RecycleAdapter.CustomViewHolder>() {

    var mStringFilterList: List<Doc>? = null
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
        val doc: Doc? = example?.get(p1)
        doc?.let { it ->
            p0.textView.text = it.titleDisplay
            p0.cardView.tag = p1
            p0.cardView.setOnClickListener {
                Toast.makeText(context, p1.toString() + " -> " + doc.titleDisplay, Toast.LENGTH_SHORT).show()
                val nextIntent = Intent(context, MainSubActivity::class.java)
                nextIntent.putExtra("doc", doc)
                context.startActivity(nextIntent)
            }
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {
        /*override fun onClick(v: View?) {
            if (v?.id == R.id.cardView) {
                Toast.makeText(context,"Test", Toast.LENGTH_SHORT).show()
            }
        }*/

        val textView: TextView = itemView.findViewById(R.id.textView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        /*init {
            cardView.setOnClickListener { this }
        }*/
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
                val filterList: ArrayList<Doc> = ArrayList()
                for (i in 0 until mStringFilterList!!.size) {
                    if (mStringFilterList?.get(i)?.titleDisplay?.toUpperCase()?.contains(constraint.toString().toUpperCase())!!) {
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
            example = results.values as List<Doc>
            notifyDataSetChanged()
        }

    }
}