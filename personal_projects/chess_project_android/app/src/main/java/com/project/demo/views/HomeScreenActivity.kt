package com.project.demo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.project.demo.databinding.ActivityHomeScreenBinding
import com.project.demo.viewmodels.HomeScreenViewModel

class HomeScreenActivity : AppCompatActivity() {
    private val viewModel: HomeScreenViewModel by viewModels { HomeScreenViewModel.Factory }
    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var cellData: ArrayList<HomeScreenCellData>
    private lateinit var adapter: HomeScreenRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel.setupListeners()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.homeScreenRecyclerView
        cellData = arrayListOf<HomeScreenCellData>()
        adapter = HomeScreenRecyclerViewAdapter(cellData)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        viewModel.homeScreenLiveData.observe(this, Observer { homeScreenCellData ->
            cellData.clear()
            cellData.addAll(homeScreenCellData)
            homeScreenCellData.let { adapter.notifyDataSetChanged() }
        })
    }
}