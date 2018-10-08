package com.example.santoshb.mytube.dummy.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.Commons
import com.example.santoshb.mytube.dummy.service.ServiceToLoadVideosInBG
import com.example.santoshb.mytube.dummy.model.Video
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.json.JSONObject

class SplashActivity : Activity() {
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        realm = Realm.getDefaultInstance()
    }

    override fun onResume() {
        super.onResume()
        val results = realm.where<Video>().findAll()
        if (results.size > 0) {
            startService(Intent(this, ServiceToLoadVideosInBG::class.java))
            Handler().postDelayed({
                startActivity(Intent(this, AllMixActivity::class.java))
                finish()
            }, 3000)
        } else {
            loadDataFromSheet()
        }
    }

    @Suppress("DEPRECATION")
    private fun loadDataFromSheet() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Videos ...")
        progressDialog.show()
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                Commons.MAIN_URL,
                null,
                Response.Listener { response ->
                    progressDialog.cancel()
                    if (response != null)
                        afterResponse(response)
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    progressDialog.cancel()
                }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun afterResponse(response: JSONObject) {
        val recordsArray = response.getJSONArray("records")
        for (i in 0 until recordsArray.length()) {
            val oneJson: JSONObject = recordsArray.getJSONObject(i)
            var id = ""
            var title = ""
            var type = ""

            if (oneJson.has("Id"))
                id = oneJson.getString("Id")

            if (oneJson.has("Title"))
                title = oneJson.getString("Title")

            if (oneJson.has("Type"))
                type = oneJson.getString("Type")


            realm.executeTransaction {
                val video = realm.createObject<Video>()
                video.id = id
                video.title = title
                video.type = type
            }
        }

        startActivity(Intent(this, AllMixActivity::class.java))
        finish()
    }
}
