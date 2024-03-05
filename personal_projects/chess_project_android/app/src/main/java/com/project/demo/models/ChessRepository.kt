package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface ChessRepository: ChessBoardInterface {
    val availableChessMoveSets: Flow<List<ChessMoveSetEntity>>
    override val chessBoardStateFlow: Flow<ChessBoardState>
    override fun tryMovingPiece(currentSquare: Square, destinationSquare: Square)
    override fun getSquaresOfValidMoves(piecesCurrentSquare: Square): Array<Square>?
}

class ChessRepositoryImpl(private val db: ChessMoveSetRelationalDatabase) :
    ChessRepository {

    val chessBoard: ChessBoardInterface = ChessBoard()

    private val chessMovesetsDao: ChessMoveSetsDao
        get() = db.chessMoveSetsDao()

    override val availableChessMoveSets: Flow<List<ChessMoveSetEntity>> = chessMovesetsDao.getAvailableChessMoveSets()

    override val chessBoardStateFlow: Flow<ChessBoardState> = chessBoard.chessBoardStateFlow

    override fun tryMovingPiece(currentSquare: Square, destinationSquare: Square) {
        chessBoard.tryMovingPiece(currentSquare, destinationSquare)
    }
    override fun getSquaresOfValidMoves(piecesCurrentSquare: Square): Array<Square>? {
        return chessBoard.getSquaresOfValidMoves(piecesCurrentSquare)
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
