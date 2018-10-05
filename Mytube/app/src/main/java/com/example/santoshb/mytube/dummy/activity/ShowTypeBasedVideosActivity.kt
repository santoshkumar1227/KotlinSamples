package com.example.santoshb.mytube.dummy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.Commons
import com.example.santoshb.mytube.dummy.adapter.VideoViewAdapter
import com.example.santoshb.mytube.dummy.model.Video
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_show_type_based_videos.*

class ShowTypeBasedVideosActivity : AppCompatActivity() {
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_type_based_videos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        realm = Realm.getDefaultInstance()

        if (intent.hasExtra("type")) {
            if (intent.getStringExtra("type") == Commons.trailer) {
                loadTrailers()
                title="Trailers"
            } else if (intent.getStringExtra("type") == Commons.teaser) {
                loadTeasers()
                title="Teasers"
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
        recyclerVideos.layoutManager = LinearLayoutManager(this)
        recyclerVideos.adapter = VideoViewAdapter(this, videos)
    }

}
