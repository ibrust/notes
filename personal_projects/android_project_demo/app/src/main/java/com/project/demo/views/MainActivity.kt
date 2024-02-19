package com.project.demo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.project.demo.databinding.ActivityMainBinding
import com.project.demo.viewmodels.DogFactAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DogFactAppViewModel by viewModels { DogFactAppViewModel.Factory }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
