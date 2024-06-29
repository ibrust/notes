package com.project.demo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.project.demo.databinding.ActivityHomeScreenBinding
import com.project.demo.viewmodels.HomeScreenViewModel
import java.lang.ref.WeakReference

class HomeScreenActivity : AppCompatActivity(), HomeScreenRecyclerViewDelegate {
    private val viewModel: HomeScreenViewModel by viewModels { HomeScreenViewModel.Factory }
    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var cellData: ArrayList<HomeScreenCellData>
    private lateinit var adapter: HomeScreenRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home Screen"

        setupRecyclerView()

        viewModel.setupListeners()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.homeScreenRecyclerView
        cellData = arrayListOf<HomeScreenCellData>()
        adapter = HomeScreenRecyclerViewAdapter(cellData)
        adapter.delegate = WeakReference(this)
        recyclerView.adapter = adapter
    }

    override fun didClickPlayButton(position: Int) {
        when (position) {
            0 -> {
                // TODO: implement a server & get two players playing
                val intent = Intent(this, ChessGameActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                // TODO: integrate stockfish or some other engine / pass in some data her to play against stockfish
                val intent = Intent(this, ChessGameActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                // TODO: create a different activity for exploration mode since it's alot different than the other 2 / needs a move tree
                // though the others could have some kind of move tree as well... that might be useful information even in a game
                // we'll see what chess.com does / what makes sense
                val intent = Intent(this, ChessGameActivity::class.java)
                startActivity(intent)
            }
            else -> return
        }
    }

    private fun setupListeners() {
        viewModel.homeScreenLiveData.observe(this, Observer { homeScreenCellData ->
            cellData.clear()
            cellData.addAll(homeScreenCellData)
            homeScreenCellData.let { adapter.notifyDataSetChanged() }
        })
    }
}