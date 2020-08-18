package com.minaseinori.minasemusic.ui.music

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.minaseinori.minasemusic.R
import kotlinx.android.synthetic.main.fragment_music.*

class MusicFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MusicViewModel::class.java)
    }

    private lateinit var adapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = MusicAdapter(
            this,
            viewModel.musicList
        )
        recyclerView.adapter = adapter
        refreshMusic()
        searchButton.setOnClickListener {
            refreshMusic()
        }
        viewModel.musicLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                recyclerView.visibility = View.VISIBLE
                viewModel.musicList.clear()
                viewModel.musicList.addAll(data)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.unable_to_find, Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshMusic()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshMusic()
    }

    private fun refreshMusic() {
        val content = searchMusicEdit.text.toString()
        viewModel.searchMusic(content)
    }
}