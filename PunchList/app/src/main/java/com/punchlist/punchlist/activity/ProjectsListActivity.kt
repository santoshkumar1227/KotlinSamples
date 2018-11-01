package com.punchlist.punchlist.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.punchlist.punchlist.R
import com.punchlist.punchlist.adapter.CommonAdapter
import com.punchlist.punchlist.model.CommonModel
import com.punchlist.punchlist.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_projects_list.*

class ProjectsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prePareData()
    }

    private fun prePareData() {
        val commonModel1 = CommonModel("1", "2", "3", "4")
        val commonModel2 = CommonModel("1", "2", "3", "4")
        val commonModel3 = CommonModel("1", "2", "3", "4")
        val commonModel4 = CommonModel("1", "2", "3", "4")
        val commonModel5 = CommonModel("1", "2", "3", "4")
        val commonModel6 = CommonModel("1", "2", "3", "4")

        val items = ArrayList<CommonModel>()
        items.add(commonModel1)
        items.add(commonModel2)
        items.add(commonModel3)
        items.add(commonModel4)
        items.add(commonModel5)
        items.add(commonModel6)
        setAdapter(items)
    }

    private fun setAdapter(items: ArrayList<CommonModel>) {
        recyclerViewProjectsList.addItemDecoration(DividerItemDecoration(this))
        recyclerViewProjectsList.adapter = CommonAdapter(this, items)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
