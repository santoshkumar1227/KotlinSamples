package com.example.santoshb.mytube.dummy.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.model.Genre
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class AllMixAdapter(private val context: Context, private val list: ArrayList<Genre>) :
        RecyclerView.Adapter<AllMixAdapter.GenreViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GenreViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.item_expand_all_mix, p0, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: GenreViewHolder, p1: Int) {
        val genre: Genre = list[p1]
        p0.tvParentTitle.text = genre.typeName
        p0.recyclerVideosList.layoutManager = LinearLayoutManager(context)
        p0.recyclerVideosList.adapter = genre.list?.let { VideoViewAdapter(context, it,false) }
    }


    inner class GenreViewHolder(view: View) : GroupViewHolder(view) {
        val tvParentTitle: TextView = view.findViewById(R.id.tvParentTitle)
        val recyclerVideosList: RecyclerView = view.findViewById(R.id.recyclerVideosList)
    }

}
