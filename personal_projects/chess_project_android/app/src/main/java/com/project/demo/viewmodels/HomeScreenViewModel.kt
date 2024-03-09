package com.project.demo.viewmodels

import android.view.View
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
import com.project.demo.views.PlayButtonData
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
    private val _homeScreenLiveData = MutableLiveData<List<HomeScreenCellData>>()
    val homeScreenLiveData: LiveData<List<HomeScreenCellData>> = _homeScreenLiveData

    fun setupListeners() {
        viewModelScope.launch {
            repository.recentGamesData.collect() { recentGames ->
                val playButtonData = repository.playButtonData.toCellData()
                _homeScreenLiveData.postValue(playButtonData)
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

fun ArrayList<HomeScreenRepository.PlayButtonData>.toCellData(): ArrayList<PlayButtonData> {
    var cellData = arrayListOf<PlayButtonData>()
    for (data in this) {
        cellData.add(data.toCellData())
    }
    return cellData
}

fun HomeScreenRepository.PlayButtonData.toCellData(): PlayButtonData {
    return PlayButtonData(
        resId = this.resId,
        title = this.buttonTitle,
        viewId = View.generateViewId()
    )
}