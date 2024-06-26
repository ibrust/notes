package com.project.demo.models

import com.project.demo.R.drawable.tape_gold_transparent_75x75
import com.project.demo.R.drawable.key_75x75_outlined_230alpha
import com.project.demo.R.drawable.trophy_paintnet_glow2_80x80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

interface HomeScreenRepository {
    val recentGamesData: StateFlow<List<ChessMoveSetEntity>>
    val playButtonData: ArrayList<PlayButtonData>

    data class PlayButtonData(
        val resId: Int,
        val buttonTitle: String
    )
}


class HomeScreenRepositoryImpl(
    private val db: ChessMoveSetRelationalDatabase,
    private val scope: CoroutineScope
) : HomeScreenRepository {

    private val chessMovesetsDao: ChessMoveSetsDao
        get() = db.chessMoveSetsDao()

    override val recentGamesData: StateFlow<List<ChessMoveSetEntity>> =
            chessMovesetsDao.getAvailableChessMoveSets().stateIn(scope, SharingStarted.Eagerly, listOf())


    // TODO: maybe this should really just be an enum, and I can do presentation for these UI-specific values in the viewmodel?
    // or maybe just flat out inject them in the viewmodel (or maybe in a presenter if I want to create one)
    override val playButtonData: ArrayList<HomeScreenRepository.PlayButtonData> = arrayListOf(
        HomeScreenRepository.PlayButtonData(trophy_paintnet_glow2_80x80, "Play Game"),
        HomeScreenRepository.PlayButtonData(tape_gold_transparent_75x75, "Practice vs AI"),
        HomeScreenRepository.PlayButtonData(key_75x75_outlined_230alpha, "Explore Moves")
    )

    fun didTapPlayButton() {}

}