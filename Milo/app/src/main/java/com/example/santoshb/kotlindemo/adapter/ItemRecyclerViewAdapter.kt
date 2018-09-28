package com.example.santoshb.kotlindemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.santoshb.kotlindemo.R


internal class ItemRecyclerViewAdapter(private val context: Context,
                                       private val arrayList: ArrayList<String>) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {

    internal inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemLabel: TextView = itemView.findViewById(R.id.item_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_row_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemLabel.text = arrayList[position]
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}