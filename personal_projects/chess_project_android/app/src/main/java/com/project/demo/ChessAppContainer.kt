package com.project.demo

import android.content.Context
import com.project.demo.models.ChessMoveSetRelationalDatabaseImpl
import com.project.demo.models.ChessGameRepository
import com.project.demo.models.ChessRepositoryImpl
import com.project.demo.models.HomeScreenRepository
import com.project.demo.models.HomeScreenRepositoryImpl
import kotlinx.coroutines.CoroutineScope

class ChessAppContainer(context: Context, scope: CoroutineScope) {
    val chessGameRepository: ChessGameRepository
    val homeScreenRepository: HomeScreenRepository

    init {
        val db = ChessMoveSetRelationalDatabaseImpl.getDatabase(context = context, scope = scope)
        chessGameRepository = ChessRepositoryImpl(db, scope = scope)
        homeScreenRepository = HomeScreenRepositoryImpl(db, scope = scope)
    }
}