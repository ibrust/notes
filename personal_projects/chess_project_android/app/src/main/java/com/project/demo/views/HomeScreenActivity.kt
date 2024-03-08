package com.project.demo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.project.demo.databinding.ActivityHomeScreenBinding
import com.project.demo.viewmodels.HomeScreenViewModel

class HomeScreenActivity : AppCompatActivity() {
    private val viewModel: HomeScreenViewModel by viewModels { HomeScreenViewModel.Factory }
    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var sectionsData: ArrayList<HomeScreenSectionsData>
    private lateinit var adapter: HomeScreenRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.homeScreenRecyclerView
        sectionsData = ArrayList()
        adapter = HomeScreenRecyclerViewAdapter(sectionsData)
        recyclerView.adapter = adapter
    }
}