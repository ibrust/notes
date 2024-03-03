package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface ChessRepository {
    val allChessMoves: Flow<List<ChessMoveEntity>>
}

class ChessRepositoryImpl(private val db: ChessMovesRelationalDatabase) :
    ChessRepository {

    private val chessMovesDao: ChessMovesDao
        get() = db.chessMovesDao()

    override val allChessMoves: Flow<List<ChessMoveEntity>> = chessMovesDao.getAllChessMoves()

    @WorkerThread
    suspend fun deleteAllChessMoves() {
        chessMovesDao.deleteAllChessMoves()
    }

    @WorkerThread
    suspend fun insert(entity: ChessMoveEntity) {
        chessMovesDao.insert(entity)
    }
}