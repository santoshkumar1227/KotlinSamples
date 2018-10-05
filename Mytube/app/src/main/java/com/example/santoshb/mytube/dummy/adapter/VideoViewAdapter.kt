package com.example.santoshb.mytube.dummy.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.model.Video
import com.google.android.youtube.player.YouTubeThumbnailView
import com.google.android.youtube.player.YouTubeInitializationResult
import android.widget.Toast
import com.example.santoshb.mytube.dummy.activity.PlayYoutubeVideoActivity
import com.google.android.youtube.player.YouTubeThumbnailLoader


class VideoViewAdapter(private val context: Context, private val list: List<Video>, private val needToShowAll: Boolean = true) : RecyclerView.Adapter<VideoViewAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_video_view, p0, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (!needToShowAll)
            if (list.size <= 2)
                list.size
            else
                2
        else
            list.size
    }

    override fun onBindViewHolder(p0: VideoViewHolder, p1: Int) {
        val video: Video = list[p1]
        p0.youTubeThumbnailView.initialize(context.resources.getString(R.string.apiKey),
                object : YouTubeThumbnailView.OnInitializedListener {
                    override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView, youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(video.id)
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                            override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {
                                youTubeThumbnailLoader.release()
                            }

                            override fun onThumbnailError(youTubeThumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                                try {
                                    Toast.makeText(context, "Not a valid youtube url.", Toast.LENGTH_SHORT).show()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }

                            }
                        })
                    }

                    override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView, youTubeInitializationResult: YouTubeInitializationResult) {
                        Toast.makeText(context, "onInitializationFailure", Toast.LENGTH_SHORT).show()
                    }
                })

        p0.tvTitle.text = video.title

        p0.linearLayout.setOnClickListener {
            val playIntent: Intent = Intent(context, PlayYoutubeVideoActivity::class.java)
            /* val bundle: Bundle = Bundle()
             bundle.putSerializable("video", video.id)
             playIntent.putExtras(bundle)*/
            playIntent.putExtra("videoId", video.id)
            context.startActivity(playIntent)
        }
    }


    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val youTubeThumbnailView: YouTubeThumbnailView = view.findViewById(R.id.youTubeThumbnailView)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val linearLayout: LinearLayout = view.findViewById(R.id.linearLayout)
    }
}