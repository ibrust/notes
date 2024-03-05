package com.project.demo.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChessBoardInterface {
    val stateFlow: Flow<ChessBoardState>

    fun tryMovingPiece(currentSquare: Square, destinationSquare: Square)
    fun getSquaresOfValidMoves(piecesCurrentSquare: Square): Array<Square>?
}

// TODO: implement castling, move number tracking / color to move tracking, en passant, end of game condition
// also figure out whether we need a coroutine context in here / other local datasource conventions
class ChessBoard(): ChessBoardInterface {
    override val stateFlow: StateFlow<ChessBoardState>
        get() = _stateFlow
    private var _stateFlow: MutableStateFlow<ChessBoardState> = MutableStateFlow(ChessBoardState(
        moveWhiteCastledOn = null,
        moveBlackCastledOn = null,
        fullMoveNumber = 1,
        colorToMove = ChessColor.WHITE,
        board = arrayOfNulls(ChessBoard.totalSquares)
    ))

    private fun getState(): ChessBoardState {
        return _stateFlow.value
    }

    init {
        setupBoard()
    }

    override fun toString(): String {
        var string = ""
        for (row in Row.values().reversed()) {
            string += "\n"
            for (column in Column.values()) {
                val emptySquare = "- "
                string += "${getState().board[Square(row, column)] ?: emptySquare}"
            }
        }
        return string
    }

    private fun setupBoard() {
        val state = getState().copy()
        val board = state.board
        for (square in Square.allSquares()) {
            board[square] = null
        }

        for (square in Row.TWO.squares()) {
            board[square] = WhitePawn()
        }
        for (square in Row.SEVEN.squares()) {
            board[square] = BlackPawn()
        }

        board[Square(Row.ONE, Column.A)] = Rook(color = ChessColor.WHITE)
        board[Square(Row.ONE, Column.H)] = Rook(color = ChessColor.WHITE)
        board[Square(Row.EIGHT, Column.A)] = Rook(color = ChessColor.BLACK)
        board[Square(Row.EIGHT, Column.H)] = Rook(color = ChessColor.BLACK)

        board[Square(Row.ONE, Column.B)] = Knight(color = ChessColor.WHITE)
        board[Square(Row.ONE, Column.G)] = Knight(color = ChessColor.WHITE)
        board[Square(Row.EIGHT, Column.B)] = Knight(color = ChessColor.BLACK)
        board[Square(Row.EIGHT, Column.G)] = Knight(color = ChessColor.BLACK)

        board[Square(Row.ONE, Column.C)] = Bishop(color = ChessColor.WHITE)
        board[Square(Row.ONE, Column.F)] = Bishop(color = ChessColor.WHITE)
        board[Square(Row.EIGHT, Column.C)] = Bishop(color = ChessColor.BLACK)
        board[Square(Row.EIGHT, Column.F)] = Bishop(color = ChessColor.BLACK)

        board[Square(Row.ONE, Column.D)] = Queen(color = ChessColor.WHITE)
        board[Square(Row.ONE, Column.E)] = King(color = ChessColor.WHITE)
        board[Square(Row.EIGHT, Column.D)] = Queen(color = ChessColor.BLACK)
        board[Square(Row.EIGHT, Column.E)] = King(color = ChessColor.BLACK)

        _stateFlow.value = state
    }

    override fun getSquaresOfValidMoves(piecesCurrentSquare: Square): Array<Square>? {
        val piece: ChessPiece = getState().board[piecesCurrentSquare] ?: return null

        val squaresOfUnobstructedMoves = getSquaresOfUnobstructedMoves(piece)
        // remove moves that check the king
        for (newSquare in squaresOfUnobstructedMoves) {
            if (doesMovePutKingInCheck(piece, newSquare)) {
                squaresOfUnobstructedMoves.remove(newSquare)
            }
        }

        return squaresOfUnobstructedMoves.toTypedArray()
    }

    override fun tryMovingPiece(currentSquare: Square, destinationSquare: Square) {
        val validMoves: Array<Square> = getSquaresOfValidMoves(currentSquare) ?: return
        if (validMoves.contains(destinationSquare)) {
            performPieceMovement(currentSquare, destinationSquare)
        }
    }

    private fun performPieceMovement(currentSquare: Square, destinationSquare: Square) {
        val state = getState().copy()
        val board = state.board
        val piece: ChessPiece = board[currentSquare] ?: return
        board[currentSquare] = null
        board[destinationSquare] = piece
        _stateFlow.value = state
    }

    private fun doesMovePutKingInCheck(piece: ChessPiece, destinationSquare: Square): Boolean {
        val currentSquare = findPiecesSquare(piece) ?: return true
        val copyOfBoard = getState().board.copy()
        copyOfBoard[currentSquare] = null
        copyOfBoard[destinationSquare] = piece

        var squareOfPiecesKing: Square? = null
        for (square in Square.allSquares()) {
            if (copyOfBoard[square] != null && copyOfBoard[square]?.color == piece.color && copyOfBoard[square] is King) {
                squareOfPiecesKing = square
            }
        }

        // scan the board for checks from any enemy pieces
        for (square in Square.allSquares()) {
            if (copyOfBoard[square] != null && copyOfBoard[square]?.color != piece.color) {
                // found an enemy piece, now see if it can take our pieces king
                val enemyPiece = copyOfBoard[square] ?: continue
                val enemyPiecesUnobstructedMoves = getSquaresOfUnobstructedMoves(enemyPiece)
                if (enemyPiecesUnobstructedMoves.contains(squareOfPiecesKing)) {
                    return true
                }
            }
        }
        return false
    }

    private fun getSquaresOfUnobstructedMoves(piece: ChessPiece): MutableList<Square> {
        val piecesCurrentSquare = findPiecesSquare(piece) ?: return mutableListOf()
        val piecesRow = piecesCurrentSquare.row
        val validSquares: MutableList<Square> = arrayListOf()
        for (move in piece.moveSet) {
            if (piece is WhitePawn && piecesRow != Row.TWO && move == Move.NORTHTWICE) {
                continue
            }
            if (piece is BlackPawn && piecesRow != Row.SEVEN && move == Move.SOUTHTWICE) {
                continue
            }
            if (move.isRange()) {
                @OptIn(ExperimentalStdlibApi::class)
                for (distance in 1..<ChessBoard.width) {
                    val destinationSquare: Square = move.calculateDestinationSquare(piecesCurrentSquare, distance) ?: continue
                    if (!isMovementPathUnobstructed(move, piece, destinationSquare)) {
                        break
                    }
                    validSquares.add(destinationSquare)
                }
            } else {
                val destinationSquare: Square = move.calculateDestinationSquare(piecesCurrentSquare, null) ?: continue
                if (isMovementPathUnobstructed(move, piece, destinationSquare)) {
                    validSquares.add(destinationSquare)
                }
            }
        }
        return validSquares
    }

    private fun isMovementPathUnobstructed(move: Move, piece: ChessPiece, destinationSquare: Square): Boolean {
        val isSquareOccupied = getState().board[destinationSquare] != null
        val destinationPiecesColor = getState().board[destinationSquare]?.color

        if (isSquareOccupied && destinationPiecesColor == piece.color) {
            return false
        }
        return !(isForwardPawnMoveAndRunsIntoPieces(move, piece)
                || isDiagonalPawnMoveIntoEmptySquare(move, piece, destinationSquare))
    }

    private fun isDiagonalPawnMoveIntoEmptySquare(move: Move, piece: ChessPiece, destinationSquare: Square): Boolean {
        if (move.isDiagonalPawnMove(piece)) {
            return getState().board[destinationSquare] == null
        }
        return false
    }

    private fun isForwardPawnMoveAndRunsIntoPieces(move: Move, piece: ChessPiece): Boolean {
        val currentSquare = findPiecesSquare(piece) ?: return false
        val board = getState().board
        if (move.isForwardPawnMove(piece)) {
            val increment = if (piece is WhitePawn) 1 else -1
            val oneSquareAhead: Square? = currentSquare + Point(x = 0, y = increment)
            val twoSquaresAhead: Square? = currentSquare + Point(x = 0, y = increment * 2)
            val oneSquareAheadBlocksMove = board[oneSquareAhead] != null
            val twoSquaresAheadBlocksMove = (move == Move.NORTHTWICE || move == Move.SOUTHTWICE) && board[twoSquaresAhead] != null
            return (oneSquareAheadBlocksMove || twoSquaresAheadBlocksMove)
        }
        return false
    }

    private fun findPiecesSquare(piece: ChessPiece): Square? {
        for (square in Square.allSquares()) {
            if (getState().board[square] == piece) {
                return square
            }
        }
        return null
    }

    companion object {
        val height = 8
        val width = 8
        val totalSquares = height * width
    }
}

