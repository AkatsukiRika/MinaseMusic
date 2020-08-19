package com.minaseinori.minasemusic.logic.network

import com.minaseinori.minasemusic.logic.model.MusicResponse
import com.minaseinori.minasemusic.logic.model.RegisterRequest
import com.minaseinori.minasemusic.logic.model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicService {
    @GET("music")
    fun searchMusic(@Query("query") query: String): Call<MusicResponse>

    @POST("user/")
    fun register(@Body request: RegisterRequest): Call<TokenResponse>
}