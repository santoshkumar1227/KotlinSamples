package com.launchship.www.examples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.launchship.www.examples.adapter.RecycleListStringAdapter
import com.launchship.www.examples.model.Doc
import kotlinx.android.synthetic.main.activity_main_sub.*

class MainSubActivity : AppCompatActivity() {
    private var adapter: RecycleListStringAdapter? = null
    private var ascending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sub)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra("doc")) {
            val doc = intent.getSerializableExtra("doc") as Doc?
            setAdapter(doc)
        }
    }

    private fun setAdapter(doc: Doc?) {
        adapter = doc?.authorDisplay?.let { RecycleListStringAdapter(this@MainSubActivity, it) }
        recyclerViewSub.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
