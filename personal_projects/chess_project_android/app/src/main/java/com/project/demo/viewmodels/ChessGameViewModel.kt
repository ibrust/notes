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
import com.project.demo.ChessAppContainer
import com.project.demo.ChessApplication
import com.project.demo.views.ChessMovesCellData
import kotlinx.coroutines.launch

class ChessGameViewModel(
    private val container: ChessAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = container.joggingAppRepository
    private val _chessMovesLiveData = MutableLiveData<List<ChessMovesCellData>>()
    val chessMovesLiveData: LiveData<List<ChessMovesCellData>> = _chessMovesLiveData

    fun setupListeners() {
        viewModelScope.launch {
            repository.availableChessMoveSets.collect() { chessMoveSets ->
                _chessMovesLiveData.postValue(chessMoveSets.map { ChessMovesCellData(
                    title = "${it.movesetId}",
                    subText = "${it.tableName}"
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

                return ChessGameViewModel(
                    (application as ChessApplication).container,
                    savedStateHandle
                ) as T
            }
        }
    }
}

