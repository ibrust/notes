package com.project.demo.models

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ChessBoardInterface {
    val chessBoardStateFlow: StateFlow<ChessBoard.State>

    fun didTouchDownOnSquare(square: Square)

    fun didReleaseOnSquare(square: Square)
}

// TODO: implement castling, move number tracking / color to move tracking, en passant, end of game condition, pawn promotion
// also figure out whether we need a coroutine context in here / other local datasource conventions
// also maybe rename this to ChessBoardModel or ChessBoardLocalDataSource?
class ChessBoard(private val scope: CoroutineScope): ChessBoardInterface {
    private var _stateFlow: MutableStateFlow<ChessBoard.State> = MutableStateFlow(ChessBoard.State())
    override val chessBoardStateFlow: StateFlow<ChessBoard.State> = _stateFlow.asStateFlow()

    private var isClickToMoveModeActivated: Boolean = false

    private fun getState(): ChessBoard.State {
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
        val board = getState().copy().board
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

        _stateFlow.value = ChessBoard.State(
            moveWhiteCastledOn = null,
            moveBlackCastledOn = null,
            fullMoveNumber = 1,
            colorToMove = ChessColor.WHITE,
            board = board,
            activatedSquare = null
        )
    }

    private fun tryMovingPiece(currentSquare: Square, destinationSquare: Square) {
        val validMoves: Array<Square> = getSquaresOfValidMoves(currentSquare) ?: return
        if (validMoves.contains(destinationSquare)) {
            performPieceMovement(currentSquare, destinationSquare)
        }
    }

    private fun getSquaresOfValidMoves(piecesCurrentSquare: Square): Array<Square>? {
        val piece: ChessPiece = getState().board[piecesCurrentSquare] ?: return null

        val squaresOfUnobstructedMoves = getSquaresOfUnobstructedMoves(piece, getState().board)
        // remove moves that check the king
        val squaresToRemove: ArrayList<Square> = arrayListOf()
        for (newSquare in squaresOfUnobstructedMoves) {
            if (doesMovePutKingInCheck(piece, newSquare)) {
                squaresToRemove.add(newSquare)
            }
        }
        squaresOfUnobstructedMoves.removeAll(squaresToRemove)

        return squaresOfUnobstructedMoves.toTypedArray()
    }

    private fun performPieceMovement(currentSquare: Square, destinationSquare: Square) {
        val state = getState().copy()
        val board = state.board
        val piece: ChessPiece = board[currentSquare] ?: return
        board[currentSquare] = null
        board[destinationSquare] = piece
        _stateFlow.value = state
    }

    override fun didTouchDownOnSquare(square: Square) {
        // things to do in this method:
        // - find out if the piece should be grabbed
        //    - depends on game mode, whether square is occupied, color of piece
        //    - doesn't depend on whether there are valid moves


        if (isClickToMoveModeActivated == false) {
            setActiveSquare(square)
        }
    }

    override fun didReleaseOnSquare(square: Square) {
        scope.launch {
            val activatedSquare: Square = _stateFlow.value.activatedSquare ?: return@launch
            if (square != activatedSquare) {
                tryMovingPiece(activatedSquare, square)

                setActiveSquare(null)
                isClickToMoveModeActivated = false
            } else {
                isClickToMoveModeActivated = !isClickToMoveModeActivated
                if (isClickToMoveModeActivated == false) {
                    setActiveSquare(null)
                }
            }
        }
    }

    private fun setActiveSquare(square: Square?) {
        val copyOfState = _stateFlow.value.copy()
        copyOfState.activatedSquare = square
        _stateFlow.value = copyOfState
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
                break
            }
        }
        squareOfPiecesKing ?: return true

        // scan the board for checks from any enemy pieces
        for (square in Square.allSquares()) {
            if (copyOfBoard[square] != null && copyOfBoard[square]?.color != piece.color) {
                // found an enemy piece, now see if it can take our pieces king
                val enemyPiece = copyOfBoard[square] ?: continue
                // need to pass in a copy of the board here...
                val enemyPiecesUnobstructedMoves = getSquaresOfUnobstructedMoves(enemyPiece, copyOfBoard)
                if (enemyPiecesUnobstructedMoves.contains(squareOfPiecesKing)) {
                    return true
                }
            }
        }
        return false
    }

    private fun getSquaresOfUnobstructedMoves(piece: ChessPiece, board: Array<ChessPiece?>): MutableList<Square> {
        val piecesCurrentSquare = findPiecesSquare(piece) ?: return mutableListOf()
        val piecesRow = piecesCurrentSquare.row
        val validSquares: MutableList<Square> = arrayListOf()
        outer@ for (move in piece.moveSet) {
            if (piece is WhitePawn && piecesRow != Row.TWO && move == Move.NORTHTWICE) {
                continue
            }
            if (piece is BlackPawn && piecesRow != Row.SEVEN && move == Move.SOUTHTWICE) {
                continue
            }
            if (move.isRange()) {
                @OptIn(ExperimentalStdlibApi::class)
                inner@ for (distance in 1..<ChessBoard.width) {
                    val destinationSquare: Square = move.calculateDestinationSquare(piecesCurrentSquare, distance) ?: continue
                    if (board[destinationSquare] != null) {
                        val destinationPiecesColor = board[destinationSquare]?.color
                        if (destinationPiecesColor != piece.color) {
                            validSquares.add(destinationSquare)
                        }
                        break@inner
                    }
                    validSquares.add(destinationSquare)
                }
            } else {
                val destinationSquare: Square = move.calculateDestinationSquare(piecesCurrentSquare, null) ?: continue
                if (board[destinationSquare] == null && !isDiagonalPawnMoveIntoEmptySquare(move, piece, destinationSquare)) {
                    validSquares.add(destinationSquare)
                } else if (board[destinationSquare] != null) {
                    val destinationPiecesColor = board[destinationSquare]?.color
                    if (destinationPiecesColor != piece.color && !isForwardPawnMoveAndRunsIntoPieces(move, piece)) {
                        validSquares.add(destinationSquare)
                    }
                }
            }
        }
        return validSquares
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

    class State(
        val moveWhiteCastledOn: Int? = null,
        val moveBlackCastledOn: Int? = null,
        val fullMoveNumber: Int = 1,
        val colorToMove: ChessColor = ChessColor.WHITE,
        val board: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.totalSquares),
        var activatedSquare: Square? = null
    ) {
        fun copy(): State {
            return State(
                moveWhiteCastledOn = this.moveWhiteCastledOn,
                moveBlackCastledOn = this.moveBlackCastledOn,
                fullMoveNumber = this.fullMoveNumber,
                colorToMove = this.colorToMove,
                board = this.board.copy(),
                activatedSquare = this.activatedSquare
            )
        }

        override fun toString(): String {
            return "${moveWhiteCastledOn}, ${moveBlackCastledOn}, ${fullMoveNumber}, ${colorToMove}, ${board}, ${activatedSquare}"
        }

        override fun hashCode(): Int {
            val sum1 = moveWhiteCastledOn.hashCode() * 31 + moveBlackCastledOn.hashCode() * 17
            val sum2 = fullMoveNumber.hashCode() * 37 + colorToMove.hashCode() * 29
            val sum3 = board.hashCode() * 11 + activatedSquare.hashCode() * 13
            return sum1 + sum2 + sum3
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is State) {
                return false
            }
            val comp1 = moveWhiteCastledOn == other.moveWhiteCastledOn && moveBlackCastledOn == other.moveBlackCastledOn
            val comp2 = fullMoveNumber == other.fullMoveNumber && colorToMove == other.colorToMove
            val comp3 = board == other.board && activatedSquare == other.activatedSquare
            return comp1 && comp2 && comp3
        }
    }
}

