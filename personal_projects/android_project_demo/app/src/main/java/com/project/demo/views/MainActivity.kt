package com.project.demo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.project.demo.viewmodels.JoggingAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: JoggingAppViewModel by viewModels { JoggingAppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
