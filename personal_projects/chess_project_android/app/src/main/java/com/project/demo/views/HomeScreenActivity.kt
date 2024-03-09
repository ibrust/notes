package com.project.demo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.project.demo.databinding.ActivityHomeScreenBinding
import com.project.demo.viewmodels.HomeScreenViewModel

class HomeScreenActivity : AppCompatActivity() {
    private val viewModel: HomeScreenViewModel by viewModels { HomeScreenViewModel.Factory }
    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var cellData: HomeScreenCellData
    private lateinit var adapter: HomeScreenRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.homeScreenRecyclerView
        cellData = HomeScreenCellData()
        adapter = HomeScreenRecyclerViewAdapter(cellData)
        recyclerView.adapter = adapter
    }
}