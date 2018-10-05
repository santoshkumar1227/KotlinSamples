package com.example.santoshb.mytube.dummy

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.santoshb.mytube.dummy.model.Video
import io.realm.Realm
import io.realm.kotlin.createObject
import org.json.JSONObject

class ServiceToLoadVideosInBG : IntentService("") {
    @SuppressLint("LongLogTag")
    override fun onHandleIntent(intent: Intent?) {
        Log.i("ServiceToLoadVideosInBG -> onHandleIntent", "Started")
        loadDataFromSheet()
    }

    @Suppress("DEPRECATION")
    private fun loadDataFromSheet() {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                Commons.MAIN_URL,
                null,
                Response.Listener { response ->
                    if (response != null)
                        afterResponse(response)
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun afterResponse(response: JSONObject) {
        val recordsArray = response.getJSONArray("records")
        var realm: Realm = Realm.getDefaultInstance()
        if (recordsArray.length() != 0) {
            realm.executeTransaction {
                realm.delete(Video::class.java)
            }

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
        }
    }
}