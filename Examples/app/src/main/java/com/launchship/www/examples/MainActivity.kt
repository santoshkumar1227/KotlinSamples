package com.launchship.www.examples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.launchship.www.examples.interfaces.ApiInterface
import com.launchship.www.examples.model.Example
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import com.launchship.www.examples.adapter.RecycleAdapter
import com.launchship.www.examples.model.Doc
import com.launchship.www.examples.retrofit.ApiServiceNetwork
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var adapter: RecycleAdapter? = null
    var example: Example? = null
    var ascending: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = Utils.getProgressBar(this, parentLayout)
        getData()
    }

    private fun setAdapter() {
        adapter = example?.response?.docs?.let { RecycleAdapter(this@MainActivity, it) }
        recyclerView.adapter = adapter
        addSearch()
    }

    private fun addSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.getFilter()?.filter(newText)
                return false
            }
        })
    }

    private fun getData() {
        progressBar?.visibility = View.VISIBLE
        ApiServiceNetwork.getInstance()?.let {
            val apiInterface: ApiInterface? = it.create(ApiInterface::class.java)
            val call = apiInterface?.getTasks()
            call?.enqueue(object : Callback<Example> {
                override fun onResponse(call: Call<Example>, response: Response<Example>) {
                    example = response.body()!!
                    setAdapter()
                    progressBar?.visibility = View.GONE
                }

                override fun onFailure(call: Call<Example>, t: Throwable) {
                    progressBar?.visibility = View.GONE
                    call.cancel()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter) {
            Toast.makeText(this@MainActivity, "Filter", Toast.LENGTH_SHORT).show()

            if (ascending == null) {
                Collections.sort(example?.response?.docs,
                        { o1: Doc, o2: Doc -> o2.authorDisplay?.size?.let { o1.authorDisplay?.size?.compareTo(it) } } as ((Doc, Doc) -> Int)?)
                ascending = false
            } else {
                Collections.reverse(example?.response?.docs);
            }
            adapter?.notifyDataSetChanged()
        }

        return true
    }
}
