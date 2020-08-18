package com.minaseinori.minasemusic.ui.play

import android.util.Log
import androidx.lifecycle.*
import com.minaseinori.minasemusic.logic.Repository
import kotlinx.coroutines.Dispatchers

class PlayViewModel : ViewModel() {
    companion object {
        const val STATUS_PAUSED = 0
        const val STATUS_PLAYING = 1
        const val STATUS_STOPPED = 2
        const val STATUS_FINISHED = 3
    }

    private val posLiveData = MutableLiveData<Long>()
    val strLiveData = Transformations.switchMap(posLiveData) { pos ->
        liveData(Dispatchers.IO) {
            val result = Result.success(Repository.formatToDigitalClock(pos))
            emit(result.getOrNull())
        }
    }

    init {
        posLiveData.value = 0
    }

    var musicTitle = ""
    var musicArtist = ""
    var musicAlbum = ""
    var musicLyrics = ""
    var musicLink = ""
    var musicDuration = 0L
    var musicDurationStr = "88:88"
    var playStatus = STATUS_STOPPED

    fun getStrTime(pos: Long) {
        posLiveData.value = pos
        Log.d("PlayViewModel", posLiveData.value.toString())
    }
}