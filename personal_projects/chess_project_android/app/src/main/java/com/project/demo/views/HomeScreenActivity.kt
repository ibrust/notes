package com.project.demo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.project.demo.R
import com.project.demo.databinding.ActivityHomeScreenBinding
import com.project.demo.databinding.ActivityMainBinding
import com.project.demo.models.Square
import com.project.demo.viewmodels.ChessGameViewModel
import java.lang.ref.WeakReference

class HomeScreenActivity : AppCompatActivity() {
    private val viewModel: ChessGameViewModel by viewModels { HomeScreenViewModel.Factory }
    private lateinit var binding: ActivityHomeScreenBindingBinding

    private lateinit var cellData: ArrayList<ChessMovesCellData>
    private lateinit var adapter: ChessMoveSetsRecyclerViewAdapter
    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel.setupListeners()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.chessMovesRecyclerView
        cellData = ArrayList()
        adapter = ChessMoveSetsRecyclerViewAdapter(cellData)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        viewModel.availableChessMoveSetsLiveData.observe(this, Observer { chessMoves ->
            cellData.clear()
            cellData.addAll(chessMoves)
            chessMoves.let { adapter.notifyDataSetChanged() }
        })
    }
}