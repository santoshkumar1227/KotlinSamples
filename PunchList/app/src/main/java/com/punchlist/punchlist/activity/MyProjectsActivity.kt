package com.punchlist.punchlist.activity

import android.graphics.Canvas
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.punchlist.punchlist.R
import com.punchlist.punchlist.adapter.CommonAdapter
import com.punchlist.punchlist.model.CommonModel
import com.punchlist.punchlist.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_my_projects.*
import android.support.v7.widget.helper.ItemTouchHelper
import com.punchlist.punchlist.utils.SwipeController
import com.punchlist.punchlist.utils.SwipeControllerActions
import android.support.v7.widget.RecyclerView
import com.punchlist.punchlist.adapter.CommonAdapterWithSwipe

class MyProjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_projects)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       // addSwipe()
        prePareData()
    }

    private fun addSwipe() {
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {

            }

        })

        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(recyclerViewMyProjectsList)

        recyclerViewMyProjectsList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

    private fun prePareData() {
        val commonModel1 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")
        val commonModel2 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")
        val commonModel3 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")
        val commonModel4 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")
        val commonModel5 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")
        val commonModel6 = CommonModel("Test1", "2", "Test3", "4", "Test5", "6")

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
        recyclerViewMyProjectsList.addItemDecoration(DividerItemDecoration(this))
        recyclerViewMyProjectsList.adapter = CommonAdapterWithSwipe(this, items)
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
