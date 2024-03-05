package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface ChessRepository {
    val availableChessMoveSets: Flow<List<ChessMoveSetEntity>>
}

class ChessRepositoryImpl(private val db: ChessMoveSetRelationalDatabase) :
    ChessRepository {

    private val chessMovesetsDao: ChessMoveSetsDao
        get() = db.chessMoveSetsDao()

    override val availableChessMoveSets: Flow<List<ChessMoveSetEntity>> = chessMovesetsDao.getAvailableChessMoveSets()

    @WorkerThread
    suspend fun deleteChessMoveset(movesetId: Int) {
        chessMovesetsDao.deleteChessMoveset(movesetId = movesetId)
    }

    @WorkerThread
    suspend fun insert(entity: ChessPositionEntity) {
        chessMovesetsDao.insert(entity)
    }
}
