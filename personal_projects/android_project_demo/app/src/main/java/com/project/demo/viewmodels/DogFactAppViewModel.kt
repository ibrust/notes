package com.project.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.demo.DogFactAppContainer
import com.project.demo.DogFactApplication
import com.project.demo.models.DogFactEntity

class DogFactAppViewModel(
    private val container: DogFactAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val repository = container.joggingAppRepository
    val joggingSessions: LiveData<List<DogFactEntity>> = repository.allDogFacts.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return DogFactAppViewModel(
                    (application as DogFactApplication).container,
                    savedStateHandle
                ) as T
            }
        }
    }
}