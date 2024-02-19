package com.project.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.demo.DogFactAppContainer
import com.project.demo.DogFactApplication
import com.project.demo.models.DogFactEntity
import com.project.demo.views.DogFactCellData
import kotlinx.coroutines.launch

class DogFactViewModel(
    private val container: DogFactAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = container.joggingAppRepository
    private val _dogFactsLiveData = MutableLiveData<List<DogFactCellData>>()
    val dogFactsLiveData: LiveData<List<DogFactCellData>> = _dogFactsLiveData

    fun setupListeners() {
        viewModelScope.launch {
            repository.allDogFacts.collect() { dogFactsEntities ->
                _dogFactsLiveData.postValue(dogFactsEntities.map { DogFactCellData(
                    title = "Title",
                    subText = it.dogFact
                )})
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return DogFactViewModel(
                    (application as DogFactApplication).container,
                    savedStateHandle
                ) as T
            }
        }
    }
}

