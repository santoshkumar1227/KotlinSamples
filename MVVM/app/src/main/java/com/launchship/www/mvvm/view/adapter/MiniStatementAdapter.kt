package com.launchship.www.mvvm.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.view.adapter.StatementAdapter.LoggingViewHolder

class MiniStatementAdapter(val context: Context, var list: List<Logging>) :
    RecyclerView.Adapter<MiniStatementAdapter.MiniStatementViewHolder>() {
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: MiniStatementViewHolder, p1: Int) {
        val logging: Logging = list[p1]
        p0.transactionAmount.text = "Amount : ${logging.getTransactionAmount().toString()}"
        p0.transactionType.text = "Transacton Type : ${logging.getTransactionType()}"
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MiniStatementViewHolder {
        return MiniStatementViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mini_statement,p0,false))
    }

    class MiniStatementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var transactionType = view.findViewById<TextView>(R.id.transactionType)
        var transactionAmount = view.findViewById<TextView>(R.id.transactionAmount)
    }

    fun updateData(t: List<Logging>?) {
        this.list = t!!
        notifyDataSetChanged()
    }
}