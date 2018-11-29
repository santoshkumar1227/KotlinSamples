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

class StatementAdapter(val context: Context, val list: List<Logging>) :
    RecyclerView.Adapter<LoggingViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LoggingViewHolder {
        return LoggingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_statement, p0, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: LoggingViewHolder, p1: Int) {
        val logging: Logging = list[p1]
        p0.transactionAmount.text = "Amount : ${logging.getTransactionAmount().toString()}"
        p0.transactionCount.text = "#${p1 + 1}"
        p0.transactionType.text = "Transacton Type : ${logging.getTransactionType()}"
        p0.transactionDate.text = "Transacton Date : ${logging.getTransactionDate()}"

    }

    class LoggingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var transactionCount = view.findViewById<TextView>(R.id.transactionCount)
        var transactionType = view.findViewById<TextView>(R.id.transactionType)
        var transactionAmount = view.findViewById<TextView>(R.id.transactionAmount)
        var transactionDate = view.findViewById<TextView>(R.id.transactionDate)
    }
}