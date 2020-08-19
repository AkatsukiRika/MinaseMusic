package com.minaseinori.minasemusic.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.minaseinori.minasemusic.logic.model.Music
import com.minaseinori.minasemusic.logic.model.RegisterRequest
import com.minaseinori.minasemusic.logic.model.TokenResponse
import com.minaseinori.minasemusic.logic.network.MinaseMusicNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.util.concurrent.TimeUnit

object Repository {
    fun searchMusic(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val musicResponse = MinaseMusicNetwork.searchMusic(query)
            val data = musicResponse.results
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure<List<Music>>(e)
        }
        emit(result)
    }

    fun register(request: RegisterRequest) = liveData(Dispatchers.IO) {
        val result = try {
            val tokenResponse = MinaseMusicNetwork.register(request)
            Result.success(tokenResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure<TokenResponse>(e)
        }
        emit(result)
    }

    fun formatToDigitalClock(ms: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(ms).toInt() % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms).toInt() % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms).toInt() % 60
        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
            seconds > 0 -> String.format("00:%02d", seconds)
            else -> {
                "00:00"
            }
        }
    }

}