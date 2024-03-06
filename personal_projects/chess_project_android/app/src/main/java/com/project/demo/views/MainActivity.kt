package com.project.demo.views

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.demo.databinding.ActivityMainBinding
import com.project.demo.models.Square
import com.project.demo.viewmodels.ChessGameViewModel
import java.lang.ref.WeakReference


interface MainActivityDelegate: ChessBoardViewDelegate {}

class MainActivity : ComponentActivity(), ChessBoardViewDelegate {

    private val viewModel: ChessGameViewModel by viewModels { ChessGameViewModel.Factory }
    private lateinit var binding: ActivityMainBinding

    private lateinit var cellData: ArrayList<ChessMovesCellData>
    private lateinit var adapter: ChessMoveSetsRecyclerViewAdapter
    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        chessBoardView = binding.chessBoardView
        chessBoardView.delegate = WeakReference(this)
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
        viewModel.chessBoardStateLiveData.observe(this, Observer { chessBoardViewState ->
            chessBoardView.update(chessBoardViewState)
        })
    }

    override fun didTouchDownOnSquare(square: Square) {
        viewModel.didTouchDownOnSquare(square)
    }

    override fun didReleaseOnSquare(square: Square) {
        viewModel.didReleaseOnSquare(square)
    }
}
