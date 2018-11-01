package com.punchlist.punchlist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.punchlist.punchlist.R
import com.punchlist.punchlist.model.CommonModel

class CommonAdapter(private val context: Context, private val items: ArrayList<CommonModel>) : RecyclerView.Adapter<CommonAdapter.CommonViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CommonViewHolder {
        return CommonViewHolder(LayoutInflater.from(context).inflate(R.layout.item_common_views, p0, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(p0: CommonViewHolder, p1: Int) {
        val commonModel = items[p1]
        p0.tv1.text = commonModel.item1
        p0.tv2.text = commonModel.item2
        p0.tv3.text = commonModel.item3
        p0.tv4.text = commonModel.item4

        if (commonModel.item5 == "" && commonModel.item6 == "") {
            p0.layout3.visibility = View.GONE
        } else {
            p0.tv5.text = commonModel.item5
            p0.tv6.text = commonModel.item6
        }
    }

    class CommonViewHolder(inflate: View) : RecyclerView.ViewHolder(inflate) {
        val tv1: TextView = inflate.findViewById(R.id.tv1)
        val tv2: TextView = inflate.findViewById(R.id.tv2)
        val tv3: TextView = inflate.findViewById(R.id.tv3)
        val tv4: TextView = inflate.findViewById(R.id.tv4)
        val tv5: TextView = inflate.findViewById(R.id.tv5)
        val tv6: TextView = inflate.findViewById(R.id.tv6)
        val layout3: LinearLayout = inflate.findViewById(R.id.layout3)
    }
}