package com.minaseinori.minasemusic.logic.network

import com.minaseinori.minasemusic.logic.model.MusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {
    @GET("music")
    fun searchMusic(@Query("query") query: String) : Call<MusicResponse>
}