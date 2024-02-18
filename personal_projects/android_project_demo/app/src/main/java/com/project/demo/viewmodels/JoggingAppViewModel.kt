package com.project.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.demo.JoggingAppContainer
import com.project.demo.JoggingApplication
import com.project.demo.models.JoggingSessionEntity

class JoggingAppViewModel(
    private val container: JoggingAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val repository = container.joggingAppRepository
    val joggingSessions: LiveData<List<JoggingSessionEntity>> = repository.allJoggingSessions.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return JoggingAppViewModel(
                    (application as JoggingApplication).container,
                    savedStateHandle
                ) as T
            }
        }
    }
}