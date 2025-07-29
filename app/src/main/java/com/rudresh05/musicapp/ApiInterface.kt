package com.rudresh05.musicapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {


    @Headers("x-rapidapi-key: 8e9b7c6680msh36153c09b5d36f8p1ebd05jsncd42b2c82d5d" ,"x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com")
    @GET("search")
    fun getData(@Query("q") query : String): Call<MyData>
}