package com.project.demo

import android.content.Context
import com.project.demo.models.ChessMovesRelationalDatabaseImpl
import com.project.demo.models.ChessMovesRepository
import com.project.demo.models.ChessMovesRepositoryImpl
import kotlinx.coroutines.CoroutineScope

class ChessAppContainer(context: Context, scope: CoroutineScope) {
    val joggingAppRepository: ChessMovesRepository

    init {
        joggingAppRepository = ChessMovesRepositoryImpl(
            ChessMovesRelationalDatabaseImpl.getDatabase(context = context, scope = scope)
        )
    }
}