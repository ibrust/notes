package com.project.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.demo.ChessAppContainer
import com.project.demo.ChessApplication
import com.project.demo.models.HomeScreenRepository
import com.project.demo.views.ChessBoardView
import com.project.demo.views.ChessMovesCellData
import com.project.demo.views.HomeScreenCellData
import kotlinx.coroutines.launch

/*
at the bottom there should be a toolbar of some sort - maybe for settings, account, return to homepage, etc.

at the top there should be a logo of some sort
app name: QueenSac.net ? maybe with a cartoon picture of a queen ramming into something
 */

class HomeScreenViewModel(
    private val container: ChessAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val repository = container.homeScreenRepository
    private val _homeScreenLiveData = MutableLiveData<HomeScreenCellData>()
    val homeScreenLiveData: LiveData<HomeScreenCellData> = _homeScreenLiveData

    init {
        _homeScreenLiveData.postValue(HomeScreenCellData(
            playButtonData = repository.playButtonData.toCellData(),
            recentGamesData = arrayListOf()
        ))
    }

    fun setupListeners() {
        viewModelScope.launch {
            repository.recentGamesData.collect() { recentGames ->
                _homeScreenLiveData.postValue(HomeScreenCellData(
                    playButtonData = repository.playButtonData.toCellData(),
                    recentGamesData = arrayListOf()
                ))
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return HomeScreenViewModel(
                    (application as ChessApplication).container,
                    savedStateHandle
                ) as T
            }
        }
    }
}

fun ArrayList<HomeScreenRepository.PlayButtonData>.toCellData(): ArrayList<HomeScreenCellData.PlayButtonData> {
    var cellData = arrayListOf<HomeScreenCellData.PlayButtonData>()
    for (data in this) {
        cellData.add(data.toCellData())
    }
    return cellData
}

fun HomeScreenRepository.PlayButtonData.toCellData(): HomeScreenCellData.PlayButtonData {
    return HomeScreenCellData.PlayButtonData(this.resId, this.buttonTitle)
}