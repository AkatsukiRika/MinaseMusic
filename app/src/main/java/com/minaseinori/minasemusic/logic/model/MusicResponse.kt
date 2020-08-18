package com.minaseinori.minasemusic.logic.model

data class MusicResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Music>
)

data class Music(
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val link: String,
    val lyrics: String
)