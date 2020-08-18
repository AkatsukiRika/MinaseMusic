package com.minaseinori.minasemusic.ui.play

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.minaseinori.minasemusic.R
import com.minaseinori.minasemusic.databinding.ActivityPlayBinding
import com.minaseinori.minasemusic.logic.Repository
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.music_item.*
import kotlinx.android.synthetic.main.music_item.musicInfo
import kotlinx.android.synthetic.main.music_item.musicTitle
import kotlinx.coroutines.*
import org.json.JSONObject
import java.time.Duration
import kotlin.concurrent.thread

class PlayActivity : AppCompatActivity() {
    private val mTAG = "PlayActivity"
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PlayViewModel::class.java)
    }
    private lateinit var binding: ActivityPlayBinding
    // Service binder
    private lateinit var playBinder: PlayService.PlayBinder
    private lateinit var connection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        // Get data from intent
        if (viewModel.musicTitle.isEmpty()) {
            viewModel.musicTitle = intent.getStringExtra("music_title") ?: ""
        }
        if (viewModel.musicArtist.isEmpty()) {
            viewModel.musicArtist = intent.getStringExtra("music_artist") ?: ""
        }
        if (viewModel.musicAlbum.isEmpty()) {
            viewModel.musicAlbum = intent.getStringExtra("music_album") ?: ""
        }
        if (viewModel.musicDuration == 0L) {
            viewModel.musicDuration = intent.getLongExtra("music_duration", 0L)
            showMusicInfo(viewModel.musicDuration)
        }
        if (viewModel.musicLyrics.isEmpty()) {
            viewModel.musicLyrics = intent.getStringExtra("music_lyrics") ?: ""
        }
        if (viewModel.musicLink.isEmpty()) {
            viewModel.musicLink = intent.getStringExtra("music_link") ?: ""
        }

        connection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                playBinder = p1 as PlayService.PlayBinder
                playBinder.initPlayer(viewModel.musicLink)
                playBinder.startInForeground()
                playBinder.getPlayer().addListener(object : Player.EventListener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        when (playbackState) {
                            ExoPlayer.STATE_ENDED -> {
                                playToggle?.setBackgroundResource(R.drawable.ic_play_mikan)
                                viewModel.playStatus = PlayViewModel.STATUS_FINISHED
                            }
                            ExoPlayer.STATE_READY -> {
                                getCurrentPos(MainScope())
                            }
                            else -> {}
                        }
                    }
                })
            }

            override fun onServiceDisconnected(p0: ComponentName?) {}
        }
        val serviceIntent = Intent(this, PlayService::class.java)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        playToggle?.let { playToggle ->
            playToggle.setOnClickListener {
                when (viewModel.playStatus) {
                    PlayViewModel.STATUS_PLAYING -> {
                        it.setBackgroundResource(R.drawable.ic_play_mikan)
                        playBinder.pause()
                        viewModel.playStatus = PlayViewModel.STATUS_PAUSED
                    }
                    PlayViewModel.STATUS_STOPPED -> {
                        it.setBackgroundResource(R.drawable.ic_pause_mikan)
                        playBinder.play()
                        viewModel.playStatus = PlayViewModel.STATUS_PLAYING
                    }
                    PlayViewModel.STATUS_PAUSED -> {
                        it.setBackgroundResource(R.drawable.ic_pause_mikan)
                        playBinder.resume()
                        viewModel.playStatus = PlayViewModel.STATUS_PLAYING
                    }
                    PlayViewModel.STATUS_FINISHED -> {
                        it.setBackgroundResource(R.drawable.ic_pause_mikan)
                        playBinder.replay()
                        viewModel.playStatus = PlayViewModel.STATUS_PLAYING
                    }
                }
            }
        }
    }

    private fun getCurrentPos(scope: CoroutineScope) {
        scope.launch {
            while (playBinder.isPlaying()) {
                val currentPos = playBinder.getCurrentPosition()
                viewModel.getStrTime(currentPos)
                delay(200)
            }
        }
    }

    private fun showMusicInfo(duration: Long) {
        val formatted = Repository.formatToDigitalClock(duration)
        viewModel.musicDurationStr = formatted
    }
}