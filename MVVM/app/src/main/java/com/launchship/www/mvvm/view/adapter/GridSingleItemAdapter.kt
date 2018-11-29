package com.launchship.www.mvvm.view.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.util.Commons
import com.launchship.www.mvvm.view.adapter.GridSingleItemAdapter.GridViewHolder
import android.widget.RelativeLayout
import com.launchship.www.mvvm.view.interfaces.ClickListener


class GridSingleItemAdapter(var context: AppCompatActivity, val list: List<String>) :
    RecyclerView.Adapter<GridViewHolder>() {

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvItem = view.findViewById<TextView>(R.id.tvItem)
        var cardView = view.findViewById<CardView>(R.id.cardView)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GridViewHolder {
        return GridViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_recycler, p0, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: GridViewHolder, p1: Int) {
        p0.tvItem.text = list[p1]
        var width = Commons.getWidthOfScreen() / 2

        p0.cardView.layoutParams = RelativeLayout.LayoutParams(width - 40, width - 120)
        p0.cardView.setPadding(10, 10, 10, 10)

        p0.cardView.setOnClickListener { (context as ClickListener).itemClick(p1) }
    }
}