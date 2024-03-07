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

// TODO: pawn promotion, win / lose, stalemate
// also would like move backtracking, board flip, board reset, square highlighting, visual tracking of taken pieces, timer, & other features on chess.com
// also figure out whether we need a coroutine context in here / other local datasource conventions
// also maybe rename this to ChessBoardModel or ChessBoardLocalDataSource?
class ChessBoard(private val scope: CoroutineScope): ChessBoardInterface {
    private var _stateFlow: MutableStateFlow<ChessBoard.State> = MutableStateFlow(ChessBoard.State())
    override val chessBoardStateFlow: StateFlow<ChessBoard.State> = _stateFlow.asStateFlow()

    private var isClickToMoveModeActivated: Boolean = false
    private var hasWhiteCastled: Boolean = false
    private var hasBlackCastled: Boolean = false
    private var enPassantSquare: Square? = null
    private var fiftyMoveRuleTracker: Int = 0
    private var mapOfPositionsReached: MutableMap<String, Int> = mutableMapOf()

    private fun getState(): ChessBoard.State {
        return _stateFlow.value
    }

    class State(
        var fullMoveNumber: Int = 1,
        var colorToMove: ChessColor = ChessColor.WHITE,
        val board: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.totalSquares),
        var activatedSquare: Square? = null,
        var result: GameResult? = null
    ) {
        fun copy(): State {
            return State(
                fullMoveNumber = this.fullMoveNumber,
                colorToMove = this.colorToMove,
                board = this.board.copy(),
                activatedSquare = this.activatedSquare,
                result = this.result
            )
        }

        override fun toString(): String {
            return "${fullMoveNumber}, ${colorToMove}, ${board}, ${activatedSquare}, ${result}"
        }

        override fun hashCode(): Int {
            val sum1 = fullMoveNumber.hashCode() * 37 + colorToMove.hashCode() * 29
            val sum2 = board.hashCode() * 11 + activatedSquare.hashCode() * 13
            val sum3 = result.hashCode() * 17
            return sum1 + sum2 + sum3
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is State) {
                return false
            }
            val comp1 = fullMoveNumber == other.fullMoveNumber && colorToMove == other.colorToMove
            val comp2 = board == other.board && activatedSquare == other.activatedSquare
            val comp3 = result == other.result
            return comp1 && comp2 && comp3
        }
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
        isClickToMoveModeActivated = false
        hasWhiteCastled = false
        hasBlackCastled = false
        enPassantSquare = null
        mapOfPositionsReached = mutableMapOf()

        val board: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.totalSquares)
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
            fullMoveNumber = 1,
            colorToMove = ChessColor.WHITE,
            board = board,
            activatedSquare = null,
            result = null
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
        var didCapturePiece = board[destinationSquare] != null
        board[destinationSquare] = piece

        if (destinationSquare == enPassantSquare && piece is Pawn) {
            val increment = if (piece is WhitePawn) -1 else 1
            board[destinationSquare + Point(x = 0, y = increment)] = null
            didCapturePiece = true
        }

        val twoSquaresEast = currentSquare + Point(x = 2, y = 0)
        val twoSquaresWest = currentSquare + Point(x = -2, y = 0)
        if ((piece is King) && (destinationSquare == twoSquaresEast || destinationSquare == twoSquaresWest)) {
            val row: Row = if (piece.color == ChessColor.BLACK) Row.EIGHT else Row.ONE
            val column: Column = if (destinationSquare == twoSquaresEast) Column.H else Column.A
            val rook = board[Square(row, column)]
            val increment = if (destinationSquare == twoSquaresEast) Point(x = -2, y = 0) else Point(x = 3, y = 0)

            board[Square(row, column)] = null
            board[Square(row, column) + increment] = rook

            if (piece.color == ChessColor.BLACK) {
                hasBlackCastled = true
            } else {
                hasWhiteCastled = true
            }
        }

        setEnPassantSquare(piece, currentSquare, destinationSquare)
        piece.hasPieceMoved = true
        fiftyMoveRuleTracker = if (!didCapturePiece) fiftyMoveRuleTracker + 1 else 0
        state.colorToMove = state.colorToMove.otherColor()
        if (state.colorToMove == ChessColor.WHITE) {
            state.fullMoveNumber += 1
        }
        trackPositionWasReached(board)
        state.result = checkForResult(state.colorToMove, state.board)

        _stateFlow.value = state
    }

    private fun trackPositionWasReached(board: Array<ChessPiece?>) {
        val hashString = board.hashString()
        val currentValue: Int? = mapOfPositionsReached[hashString]
        mapOfPositionsReached[hashString] = (currentValue ?: 0) + 1
    }

    private fun checkForStalemate(colorToMove: ChessColor, board: Array<ChessPiece?>): Boolean {
        val pieces = board.getPiecesForColor(colorToMove)
        val potentialMoves: MutableList<Square> = mutableListOf()
        for (piece in pieces) {
            potentialMoves.addAll(getSquaresOfUnobstructedMoves(piece, board))
        }
        return potentialMoves.isEmpty()
    }

    private fun checkForResult(colorToMove: ChessColor, board: Array<ChessPiece?>): GameResult? {
        val repetitions = mapOfPositionsReached[board.hashString()]
        val isStalemate = checkForStalemate(colorToMove, board)
        if (isStalemate || repetitions == 3 || fiftyMoveRuleTracker >= 50) {
            return GameResult.DRAW
        }
        return null
    }

    private fun setEnPassantSquare(piece: ChessPiece, currentSquare: Square, destinationSquare: Square) {
        if (piece is Pawn) {
            val twoSquaresSouth = currentSquare + Point(x = 0, y = -2)
            val twoSquaresNorth = currentSquare + Point(x = 0, y = 2)
            if (destinationSquare == twoSquaresSouth) {
                enPassantSquare = currentSquare + Point(x = 0, y = -1)
            } else if (destinationSquare == twoSquaresNorth) {
                enPassantSquare = currentSquare + Point(x = 0, y = 1)
            } else {
                enPassantSquare = null
            }
        } else {
            enPassantSquare =  null
        }
    }

    override fun didTouchDownOnSquare(square: Square) {
        val board = getState().board
        val piece = board[square] ?: return

        if (GameMode.currentGameMode == GameMode.COMPETITIVE
                    && piece.color != ChessColor.playerColor) {
            return
        }
        if (piece.color != _stateFlow.value.colorToMove) {
            return
        }

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
        val currentSquare = findPiecesSquare(piece) ?: return mutableListOf()
        val piecesRow = currentSquare.row
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
                    val destinationSquare: Square = move.calculateDestinationSquare(currentSquare, distance) ?: continue
                    if (board[destinationSquare] != null) {
                        val destinationPiecesColor = board[destinationSquare]?.color
                        if (destinationPiecesColor != piece.color) {
                            validSquares.add(destinationSquare)
                        }
                        break@inner
                    }
                    validSquares.add(destinationSquare)
                }
            } else if (move.isCastling()) {
                if (canStillCastle(piece, move)
                        && castlePathIsClear(move, currentSquare, board)
                        && !enemyPiecesPreventCastling(piece, move, currentSquare, board)) {
                    val increment: Int = if (move == Move.CASTLEQUEENSIDE) -2 else 2
                    val destinationSquare: Square = currentSquare + Point(x = increment, y = 0) ?: continue
                    validSquares.add(destinationSquare)
                }
            } else if (move == Move.ENPASSANT) {
                val unwrappedEnPassantSquare = enPassantSquare ?: continue

                if (canPerformEnPassant(piece, currentSquare, unwrappedEnPassantSquare)) {
                    validSquares.add(unwrappedEnPassantSquare)
                }
            } else {
                val destinationSquare: Square = move.calculateDestinationSquare(currentSquare, null) ?: continue
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

    private fun canPerformEnPassant(piece: ChessPiece, currentSquare: Square, enPassantSquare: Square): Boolean {
        if (piece is WhitePawn) {
            return (currentSquare == enPassantSquare + Point(x = 1, y = -1) || currentSquare == enPassantSquare + Point(x = -1, y = -1))
        } else if (piece is BlackPawn) {
            return (currentSquare == enPassantSquare + Point(x = 1, y = 1) || currentSquare == enPassantSquare + Point(x = -1, y = 1))
        }
        return false
    }

    private fun enemyPiecesPreventCastling(king: ChessPiece, move: Move, currentSquare: Square, board: Array<ChessPiece?>): Boolean {
        val increment = if (move == Move.CASTLEKINGSIDE) 1 else -1
        val oneSquareAhead: Square = currentSquare + Point(x = increment, y = 0) ?: return true
        val twoSquaresAhead: Square = currentSquare + Point(x = increment * 2, y = 0) ?: return true

        // scan the board for checks from any enemy pieces
        for (square in Square.allSquares()) {
            if (board[square] != null && board[square]?.color != king.color) {
                // found an enemy piece, now see if it can take our pieces king
                val enemyPiece = board[square] ?: continue
                if (enemyPiece is King) continue
                val enemyPiecesUnobstructedMoves = getSquaresOfUnobstructedMoves(enemyPiece, board)
                if (enemyPiecesUnobstructedMoves.contains(currentSquare)
                        || enemyPiecesUnobstructedMoves.contains(oneSquareAhead)
                        || enemyPiecesUnobstructedMoves.contains(twoSquaresAhead)) {
                    return true
                }
            }
        }
        return false
    }

    private fun castlePathIsClear(move: Move, currentSquare: Square, board: Array<ChessPiece?>): Boolean {
        var pathIsClear: Boolean = false
        if (move == Move.CASTLEQUEENSIDE) {
            pathIsClear = (board[currentSquare + Point(x = -1, y = 0)] == null
                    && board[currentSquare + Point(x = -2, y = 0)] == null
                    && board[currentSquare + Point(x = -3, y = 0)] == null)
        } else if (move == Move.CASTLEKINGSIDE) {
            pathIsClear = (board[currentSquare + Point(x = 1, y = 0)] == null
                    && board[currentSquare + Point(x = 2, y = 0)] == null)
        }
        return pathIsClear
    }

    private fun canStillCastle(piece: ChessPiece, move: Move): Boolean {
        if (piece !is King || piece.hasPieceMoved) return false
        if (!move.isCastling()) return false
        val board = _stateFlow.value.board
        val hasQueensRookMoved: Boolean
        val hasKingsRookMoved: Boolean
        if (piece.color == ChessColor.WHITE) {
            hasQueensRookMoved = board[Square(Row.ONE, Column.A)]?.hasPieceMoved == false
            hasKingsRookMoved = board[Square(Row.ONE, Column.H)]?.hasPieceMoved == false
            val targetRookHasntMoved: Boolean = if (move == Move.CASTLEKINGSIDE) hasKingsRookMoved else hasQueensRookMoved
            return (!hasWhiteCastled && targetRookHasntMoved)
        } else {
            hasQueensRookMoved = board[Square(Row.EIGHT, Column.A)]?.hasPieceMoved == false
            hasKingsRookMoved = board[Square(Row.EIGHT, Column.H)]?.hasPieceMoved == false
            val targetRookHasntMoved: Boolean = if (move == Move.CASTLEKINGSIDE) hasKingsRookMoved else hasQueensRookMoved
            return (!hasBlackCastled && targetRookHasntMoved)
        }

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

