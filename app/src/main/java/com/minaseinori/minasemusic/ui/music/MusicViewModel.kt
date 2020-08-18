package com.minaseinori.minasemusic.ui.music

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.minaseinori.minasemusic.logic.Repository
import com.minaseinori.minasemusic.logic.model.Music
import retrofit2.http.Query

class MusicViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val musicList = ArrayList<Music>()

    val musicLiveData = Transformations.switchMap(searchLiveData) { query ->
        Log.d("MusicViewModel", "map switched")
        Repository.searchMusic(query)
    }

    fun searchMusic(query: String) {
        searchLiveData.value = query
    }
}