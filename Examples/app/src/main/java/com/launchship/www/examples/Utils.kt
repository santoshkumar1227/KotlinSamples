package com.launchship.www.examples

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout


object Utils {
    fun getProgressBar(context: Context, viewGroup: ViewGroup): ProgressBar {
        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        viewGroup.addView(progressBar, params)
        progressBar.visibility = View.VISIBLE  //To show ProgressBar
        return progressBar
    }
}