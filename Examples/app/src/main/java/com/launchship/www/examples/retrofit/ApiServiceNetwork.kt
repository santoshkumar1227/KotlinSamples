package com.launchship.www.examples.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceNetwork private constructor() {
    companion object {
        private var retrofit : Retrofit? = null
        fun getInstance(): Retrofit? {
            if (retrofit == null) {
                val client = OkHttpClient.Builder().build()
                retrofit = Retrofit.Builder()
                        .baseUrl("http://api.plos.org") //http://api.plos.org/search?q=title:DNA
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
            }
            return retrofit
        }
    }
}
