package com.launchship.www.examples.interfaces

import com.launchship.www.examples.model.Example
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("search?q=title:DNA")
    fun getTasks(): Call<Example>
}