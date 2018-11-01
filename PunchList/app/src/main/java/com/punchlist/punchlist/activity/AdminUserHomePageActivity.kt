package com.punchlist.punchlist.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.punchlist.punchlist.R
import com.punchlist.punchlist.adapter.HomePageGridItemAdapter
import com.punchlist.punchlist.model.GridItem
import kotlinx.android.synthetic.main.activity_admin_user_home_page.*

class AdminUserHomePageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_home_page)

        prePareDataForRecyclerView()
    }

    private fun prePareDataForRecyclerView() {
        val items = ArrayList<GridItem>()

        val item1 = GridItem("Dashboard", R.mipmap.ic_launcher)
        val item2 = GridItem("New User", R.mipmap.ic_launcher)
        val item3 = GridItem("New Project", R.mipmap.ic_launcher)
        val item4 = GridItem("New Department", R.mipmap.ic_launcher)
        val item5 = GridItem("Test", R.mipmap.ic_launcher)
        val item6 = GridItem("Test", R.mipmap.ic_launcher)

        items.add(item1)
        items.add(item2)
        items.add(item3)
        items.add(item4)
        items.add(item5)
        items.add(item6)

        setAdapter(items)
    }

    private fun setAdapter(items: ArrayList<GridItem>) {
        recyclerViewAdminHomePage.layoutManager = GridLayoutManager(this, 2)
        recyclerViewAdminHomePage.adapter = HomePageGridItemAdapter(applicationContext, items)
    }


}
