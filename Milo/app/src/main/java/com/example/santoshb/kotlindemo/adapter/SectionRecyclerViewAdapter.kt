package com.example.santoshb.kotlindemo.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.example.santoshb.kotlindemo.model.SectionModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.santoshb.kotlindemo.R


internal class SectionRecyclerViewAdapter(private val context: Context,
                                          private val sectionModelArrayList: ArrayList<SectionModel>) : RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder>() {

    internal inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionLabel: TextView = itemView.findViewById(R.id.section_label)
        val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_recycler_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.section_custom_row_layout, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val sectionModel = sectionModelArrayList[position]
        holder?.let {
            holder.sectionLabel.text = sectionModel.sectionLabel

            //recycler view for items
            holder.itemRecyclerView.setHasFixedSize(true)
            holder.itemRecyclerView.isNestedScrollingEnabled = false


            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.itemRecyclerView.layoutManager = linearLayoutManager

            val adapter = ItemRecyclerViewAdapter(context, sectionModel.itemArrayList)
            holder.itemRecyclerView.adapter = adapter
        }

    }

    override fun getItemCount(): Int {
        return sectionModelArrayList.size
    }


}