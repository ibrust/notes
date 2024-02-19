package com.project.demo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.demo.databinding.ActivityMainBinding
import com.project.demo.viewmodels.DogFactViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DogFactViewModel by viewModels { DogFactViewModel.Factory }
    private lateinit var binding: ActivityMainBinding

    private lateinit var cellData: ArrayList<DogFactCellData>
    private lateinit var adapter: DogFactRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel.setupListeners()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.dogFactRecyclerView
        cellData = ArrayList<DogFactCellData>()
        adapter = DogFactRecyclerViewAdapter(cellData)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        viewModel.dogFactsLiveData.observe(this, Observer {dogFacts ->
            cellData.clear()
            cellData.addAll(dogFacts)
            dogFacts.let { adapter.notifyDataSetChanged() }
        })
    }
}
