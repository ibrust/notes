package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChessRepository: ChessBoardInterface {
    val availableChessMoveSets: Flow<List<ChessMoveSetEntity>>
}

class ChessRepositoryImpl(private val db: ChessMoveSetRelationalDatabase, private val scope: CoroutineScope) :
    ChessRepository {

    // TODO: inject a coroutine scope maybe? maybe read about localdatasource designs
    // and maybe rename the class to ChessBoardLocalDataSource...
    private val chessBoard: ChessBoardInterface = ChessBoard(scope)

    private val chessMovesetsDao: ChessMoveSetsDao
        get() = db.chessMoveSetsDao()

    override val availableChessMoveSets: Flow<List<ChessMoveSetEntity>> = chessMovesetsDao.getAvailableChessMoveSets()

    override val chessBoardStateFlow: StateFlow<ChessBoard.State> = chessBoard.chessBoardStateFlow

    override fun didTouchDownOnSquare(square: Square) {
        chessBoard.didTouchDownOnSquare(square)
    }

    override fun didReleaseOnSquare(square: Square) {
        chessBoard.didReleaseOnSquare(square)
    }

    @WorkerThread
    suspend fun deleteChessMoveset(movesetId: Int) {
        chessMovesetsDao.deleteChessMoveset(movesetId = movesetId)
    }

    @WorkerThread
    suspend fun insert(entity: ChessPositionEntity) {
        chessMovesetsDao.insert(entity)
    }
}
