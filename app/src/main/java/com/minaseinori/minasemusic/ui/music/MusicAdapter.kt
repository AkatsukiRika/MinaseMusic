package com.minaseinori.minasemusic.ui.music

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.minaseinori.minasemusic.R
import com.minaseinori.minasemusic.logic.model.Music
import com.minaseinori.minasemusic.ui.play.PlayActivity

class MusicAdapter(private val fragment: Fragment, private val musicList: List<Music>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicInfo: TextView = view.findViewById(R.id.musicInfo)
        val musicTitle: TextView = view.findViewById(R.id.musicTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val music = musicList[position]
            val intent = Intent(parent.context, PlayActivity::class.java).apply {
                putExtra("music_title", music.title)
                putExtra("music_artist", music.artist)
                putExtra("music_album", music.album)
                putExtra("music_duration", music.duration)
                putExtra("music_link", music.link)
                putExtra("music_lyrics", music.lyrics)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicList[position]
        holder.musicInfo.text = "${music.artist} - ${music.album}"
        holder.musicTitle.text = music.title
    }

    override fun getItemCount() = musicList.size
}