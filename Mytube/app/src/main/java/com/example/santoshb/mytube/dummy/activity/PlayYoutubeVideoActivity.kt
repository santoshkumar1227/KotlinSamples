package com.example.santoshb.mytube.dummy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.model.Video
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_play_youtube_video.*
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity


class PlayYoutubeVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val RECOVERY_DIALOG_REQUEST = 1
    private var video: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play_youtube_video)

        if (intent.hasExtra("videoId")) {
            video = intent.getStringExtra("videoId")
        }

        video?.let {
            youTubePlayerView.initialize(getString(R.string.apiKey), this)
        }
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
        if (!p2) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            if (p1 != null) {
                p1.loadVideo(video)
                // Hiding player controls
                p1.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        if (p1 != null) {
            if (p1.isUserRecoverableError) {
                p1.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val errorMessage = String.format(
                        getString(R.string.error_player), p1.toString())
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        video?.let {
            youTubePlayerView.initialize(getString(R.string.apiKey), this)
        }
    }

}
