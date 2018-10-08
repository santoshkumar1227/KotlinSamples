package com.example.santoshb.mytube.dummy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.Commons
import com.example.santoshb.mytube.dummy.adapter.VideoViewAdapter
import com.example.santoshb.mytube.dummy.fragment.ItemListDialogFragment
import com.example.santoshb.mytube.dummy.model.ItemWithCheck
import com.example.santoshb.mytube.dummy.model.Video
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_show_type_based_videos.*


class ShowTypeBasedVideosActivity : AppCompatActivity(), ItemListDialogFragment.Listener {


    lateinit var realm: Realm
    val mPageSize = 10
    var paginationVideos: ArrayList<Video> = ArrayList<Video>()
    var type: String = ""
    override fun onItemClicked(list: ArrayList<ItemWithCheck>) {
        val finalList = ArrayList<String>()
        for (item in list) {
            if (item.isChecked) {
                finalList.add(item.title)
            }
        }
        if (finalList.size != 0) {
            val array = finalList.toArray(arrayOfNulls<String>(finalList.size))
            updateData(array)
        }
    }

    private fun updateData(list: Array<String?>) {
        val results = realm
                .where<Video>()
                .contains("type", type)
                .`in`("language", list).findAll()

        paginationVideos.clear()

        loadAdapter(results)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_type_based_videos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        realm = Realm.getDefaultInstance()

        if (intent.hasExtra("type")) {
            if (intent.getStringExtra("type") == Commons.trailer) {
                loadTrailers()
                title = "Trailers"
                type = Commons.trailer
            } else if (intent.getStringExtra("type") == Commons.teaser) {
                loadTeasers()
                title = "Teasers"
                type = Commons.teaser
            }
        }

    }

    private fun loadTeasers() {
        val resultsTeaser: List<Video> = realm.where<Video>()
                .contains("type", Commons.teaser)
                .findAll()

        loadAdapter(resultsTeaser)
    }

    private fun loadTrailers() {
        val resultsTrailer: List<Video> = realm.where<Video>()
                .contains("type", Commons.trailer)
                .findAll()
        loadAdapter(resultsTrailer)
    }


    private fun loadAdapter(videos: List<Video>) {
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerVideos.layoutManager = linearLayoutManager

        if (mPageSize >= videos.size) {
            recyclerVideos.adapter = VideoViewAdapter(this, videos)
        } else {
            if (paginationVideos.isEmpty()) {
                for (i in 0 until mPageSize) {
                    paginationVideos.add(videos[i])
                }
            }

            recyclerVideos.adapter = VideoViewAdapter(this, paginationVideos)

            recyclerVideos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // if dy>0 scrolling down side
                    // if dy<0 scrolling upper side
                    if (dy > 0 && linearLayoutManager.findLastCompletelyVisibleItemPosition() == 1 && paginationVideos.size < videos.size) {
                        if (paginationVideos.size + mPageSize < videos.size) {
                            for (i in paginationVideos.size..paginationVideos.size + mPageSize) {
                                paginationVideos.add(videos[i])
                            }
                        } else {
                            for (i in paginationVideos.size until videos.size) {
                                paginationVideos.add(videos[i])
                            }
                        }
                        recyclerVideos.adapter?.notifyDataSetChanged()
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.filter -> {
                val listForCheckBoxes = ArrayList<ItemWithCheck>()
                listForCheckBoxes.add(ItemWithCheck("Telugu", false))
                listForCheckBoxes.add(ItemWithCheck("English", false))
                val bottomSheetFragment: ItemListDialogFragment = ItemListDialogFragment.newInstance(listForCheckBoxes)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
