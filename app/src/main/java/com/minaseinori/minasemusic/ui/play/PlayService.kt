package com.minaseinori.minasemusic.ui.play

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.minaseinori.minasemusic.MinaseMusicApplication
import com.minaseinori.minasemusic.R

class PlayService : Service() {
    private val mTAG = "PlayService"
    private val mBinder = PlayBinder()
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var concatenatingMediaSource: ConcatenatingMediaSource
    private lateinit var mediaSource: ExtractorMediaSource
    lateinit var player: ExoPlayer

    inner class PlayBinder : Binder() {
        fun startInForeground() {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val playServiceStr = resources.getString(R.string.play_service)
                val channel = NotificationChannel(
                    "play_service",
                    playServiceStr,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            }
            val context = MinaseMusicApplication.context
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.component = ComponentName(context, PlayActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            val pi = PendingIntent.getActivity(context, 0, intent, 0)
            val contentTitleStr = resources.getString(R.string.notification_title)
            val contentTextStr = resources.getString(R.string.notification_text)
            val notification = NotificationCompat.Builder(context, "play_service")
                .setContentTitle(contentTitleStr)
                .setContentText(contentTextStr)
                .setSmallIcon(R.mipmap.ic_music_app)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_music_app))
                .setContentIntent(pi)
                .build()
            notification.flags = Notification.FLAG_AUTO_CANCEL
            startForeground(1, notification)
        }

        fun initPlayer(musicLink: String) {
            val context = MinaseMusicApplication.context
            player = SimpleExoPlayer.Builder(context).build()
            concatenatingMediaSource = ConcatenatingMediaSource()
            defaultDataSourceFactory = DefaultDataSourceFactory(context, "audio/mpeg")
            mediaSource = ExtractorMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(Uri.parse(musicLink))
            concatenatingMediaSource.addMediaSource(mediaSource)
            player.prepare(concatenatingMediaSource)
            Log.d(mTAG, "PlayService: musicLink is $musicLink")
        }

        fun isPlaying() = player.isPlaying

        fun getCurrentPosition() = player.currentPosition

        fun getPlayer() = player

        fun play() {
            player.playWhenReady = true
        }

        fun pause() {
            player.playWhenReady = false
        }

        fun resume() {
            player.playWhenReady = true
        }

        fun replay() {
            player.seekTo(0)
            player.playWhenReady = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }
}