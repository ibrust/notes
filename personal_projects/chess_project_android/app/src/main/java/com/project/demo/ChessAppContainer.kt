package com.project.demo

import android.content.Context
import com.project.demo.models.ChessMoveSetRelationalDatabaseImpl
import com.project.demo.models.ChessRepository
import com.project.demo.models.ChessRepositoryImpl
import kotlinx.coroutines.CoroutineScope

class ChessAppContainer(context: Context, scope: CoroutineScope) {
    val joggingAppRepository: ChessRepository

    init {
        joggingAppRepository = ChessRepositoryImpl(
            ChessMoveSetRelationalDatabaseImpl.getDatabase(context = context, scope = scope)
        )
    }
}