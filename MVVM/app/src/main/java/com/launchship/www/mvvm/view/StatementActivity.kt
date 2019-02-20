package com.launchship.www.mvvm.view

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.view.adapter.StatementAdapter
import com.launchship.www.mvvm.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_statement.*

class StatementActivity : AppCompatActivity() {

    lateinit var homePageViewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel::class.java)
        loadStatement(homePageViewModel.getStatement(this))
    }

    private fun loadStatement(statement: List<Logging>?) {
        recyclerViewStatement.adapter = statement?.let { StatementAdapter(this, it) }
    }
}
