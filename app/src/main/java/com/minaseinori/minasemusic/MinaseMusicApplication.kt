package com.minaseinori.minasemusic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MinaseMusicApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}