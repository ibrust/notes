package com.project.demo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.project.demo.viewmodels.DogFactAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DogFactAppViewModel by viewModels { DogFactAppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
