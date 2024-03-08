package com.project.demo.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.project.demo.ChessAppContainer
import com.project.demo.ChessApplication

/*
so I want this to have a recycler view with different sections
sections will include:
- section for playing games - can play against computer, against other players (eventually), or for exploring the chess board
- section for exploring recent games - will save last however many games in a database for viewing
- section for exploring / creating movesets

for the recent games / move sets sections there will be an arrow, and if they click it this'll push another activity
with a longer list of games / move sets and more in depth features for that function

for move sets... you'll need the ability to delete / add move sets
add will take you to the explorer, and there will be a button to save your move sets or edit them, or something like this
______________

then at the bottom th ere should be a toolbar for settings, account, and homepage (I think...)
_______________

but FOR NOW let's juts do the 'play game' section
I suppose we could also do the recent games section... we could refactor the database for that purpose
and then we can deal with the movesets later, since that's more difficult

on movesets screen let's have icons of small chessboards with the ending position? or a random position in the middle of the game?

app names: KING CRUSHER ? FALLEN KING ? KING SACRIFICE ? QUEEN SAC
I think Queen Sac comes closest

 */

class HomeScreenViewModel(
    private val container: ChessAppContainer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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