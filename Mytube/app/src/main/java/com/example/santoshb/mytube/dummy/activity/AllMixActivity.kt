package com.example.santoshb.mytube.dummy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.Commons
import com.example.santoshb.mytube.dummy.adapter.AllMixAdapter
import com.example.santoshb.mytube.dummy.model.Genre
import com.example.santoshb.mytube.dummy.model.Video
import kotlinx.android.synthetic.main.activity_all_mix.*
import io.realm.Realm
import io.realm.kotlin.where


class AllMixActivity : AppCompatActivity() {
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mix)
        realm = Realm.getDefaultInstance()
        loadFromDB()
    }

    private fun loadFromDB() {
        val resultsTrailer: List<Video> = realm.where<Video>()
                .contains("type", Commons.trailer)
                .findAll()

        val resultsTeaser: List<Video> = realm.where<Video>()
                .contains("type", Commons.teaser)
                .findAll()

        prepareData(resultsTrailer, resultsTeaser)
    }

    private fun prepareData(trailersList: List<Video>, teasersList: List<Video>) {
        val list = ArrayList<Genre>()

        val trailers: Genre = Genre("Trailers", trailersList)
        val teasers: Genre = Genre("Teasers", teasersList)

        list.add(trailers)
        list.add(teasers)

        val adapter: AllMixAdapter = AllMixAdapter(this, list)
        recyclerViewAllMix.layoutManager = LinearLayoutManager(this)
        recyclerViewAllMix.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.navigation_Teasers -> {
                val intent: Intent = Intent(this, ShowTypeBasedVideosActivity::class.java)
                intent.putExtra("type", Commons.teaser)
                startActivity(intent)
            }

            R.id.navigation_Trailers -> {
                val intent: Intent = Intent(this, ShowTypeBasedVideosActivity::class.java)
                intent.putExtra("type", Commons.trailer)
                startActivity(intent)
            }

            else -> {
                return false
            }
        }
        return true
    }
}
