package com.project.demo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.demo.databinding.ActivityChessGameBinding
import com.project.demo.models.Square
import com.project.demo.viewmodels.ChessGameViewModel
import java.lang.ref.WeakReference


interface ChessGameActivityDelegate: ChessBoardViewDelegate {}

class ChessGameActivity : AppCompatActivity(), ChessBoardViewDelegate {

    private val viewModel: ChessGameViewModel by viewModels { ChessGameViewModel.Factory }
    private lateinit var binding: ActivityChessGameBinding

    private lateinit var cellData: ArrayList<ChessMovesCellData>
    private lateinit var adapter: ChessMoveSetsRecyclerViewAdapter
    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChessGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Online 1v1"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
