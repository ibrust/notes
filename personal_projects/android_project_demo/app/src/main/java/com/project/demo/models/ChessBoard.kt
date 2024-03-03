package com.project.demo.models

import kotlin.math.abs
import kotlin.math.floor


class ChessBoard() {

    var board: Array<ChessPiece?> = arrayOfNulls(ChessBoard.totalSquares)

    init {
        setupBoard()
    }

    override fun toString(): String {
        var string = ""
        for (row in Row.values().reversed()) {
            string += "\n"
            for (column in Column.values()) {
                val index = getSquaresIndex(row, column)
                val emptySquare = "- "
                string += "${board[index] ?: emptySquare}"
            }
        }
        return string
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setupBoard() {
        for (index in 0..<ChessBoard.totalSquares) {
            board[index] = null
        }

        for (index in Row.TWO.indices()) {
            board[index] = WhitePawn()
        }
        for (index in Row.SEVEN.indices()) {
            board[index] = BlackPawn()
        }

        board[getSquaresIndex(Row.ONE, Column.A)] = Rook(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.H)] = Rook(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.A)] = Rook(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.H)] = Rook(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.B)] = Knight(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.G)] = Knight(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.B)] = Knight(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.G)] = Knight(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.C)] = Bishop(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.F)] = Bishop(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.C)] = Bishop(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.F)] = Bishop(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.D)] = Queen(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.E)] = King(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.D)] = Queen(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.E)] = King(color = ChessColor.BLACK)
    }

    fun getPiece(row: Row, column: Column): ChessPiece? {
        return board[getSquaresIndex(row, column)]
    }

    fun movePiece(piece: ChessPiece?, destinationIndex: Int) {
        val unwrappedPiece: ChessPiece = piece ?: return
        val currentIndex: Int = getPiecesIndex(unwrappedPiece) ?: return
        board[currentIndex] = null
        board[destinationIndex] = unwrappedPiece
    }

    fun getValidMovesForPiece(piece: ChessPiece): Array<Int> {
        // get moves that are on the board / not obstructed by your own piece
        var candidateMoveIndices = getCandidateMoveIndicesForPiece(piece)

        // remove moves that check the king
        for (moveIndex in candidateMoveIndices) {
            if (doesMovePutKingInCheck(piece, moveIndex)) {
                candidateMoveIndices.remove(moveIndex)
            }
        }

        return candidateMoveIndices.toTypedArray()
    }

    fun doesMovePutKingInCheck(piece: ChessPiece, destinationIndex: Int): Boolean {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return true
        val copyOfBoard = board.toList().toTypedArray()
        copyOfBoard[piecesCurrentIndex] = null
        copyOfBoard[destinationIndex] = piece

        // get index of the king
        var enemyKingsPosition: Int = 0
        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.totalSquares) {
            if (copyOfBoard[index] != null && copyOfBoard[index]?.color != piece.color && copyOfBoard[index] is King) {
                enemyKingsPosition = index
            }
        }

        // scan the board for checks from any enemy pieces
        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.totalSquares) {
            if (copyOfBoard[index] != null && copyOfBoard[index]?.color != piece.color) {
                // found an enemy piece, now see if it can take the king
                val enemyPiece = copyOfBoard[index] ?: continue
                val enemyPieceCandidateMoves = getCandidateMoveIndicesForPiece(enemyPiece)
                if (enemyPieceCandidateMoves.contains(enemyKingsPosition)) {
                    return true
                }
            }
        }
        return false
    }

    fun getCandidateMoveIndicesForPiece(piece: ChessPiece): MutableList<Int> {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return mutableListOf()
        var validMoveIndices: MutableList<Int> = arrayListOf()
        val piecesRow = getRow(piecesCurrentIndex)
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
                    val destinationIndex: Int = calculateMovesDestinationIndex(piecesCurrentIndex, move, distance) ?: continue
                    if (!isMovementPathValid(move, piece, destinationIndex)) {
                        break
                    }
                    validMoveIndices.add(destinationIndex)
                }
            } else {
                val destinationIndex: Int = calculateMovesDestinationIndex(piecesCurrentIndex, move, null) ?: continue
                if (isMovementPathValid(move, piece, destinationIndex)) {
                    validMoveIndices.add(destinationIndex)
                }
            }
        }
        return validMoveIndices
    }

    fun isMovementPathValid(move: Move, piece: ChessPiece, destinationIndex: Int): Boolean {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return false
        // check that the move is on the board
        if (!doesMoveStayOnBoard(move, startingIndex = piecesCurrentIndex, destinationIndex = destinationIndex)) {
            return false
        }

        // check if destination is occupied by your own piece
        val isSquareOccupied = board[destinationIndex] != null
        val destinationPiecesColor = board[destinationIndex]?.color
        if (isSquareOccupied && destinationPiecesColor == piece.color) {
            return false
        }

        return !(isForwardPawnMoveAndRunsIntoPieces(move, piece)
                || isDiagonalPawnMoveIntoEmptySquare(move, piece, destinationIndex))
    }

    fun isDiagonalPawnMoveIntoEmptySquare(move: Move, piece: ChessPiece, destinationIndex: Int): Boolean {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return true
        if (move.isDiagonalPawnMove(piece)) {
            return board[destinationIndex] == null
        }
        return false
    }

    fun isForwardPawnMoveAndRunsIntoPieces(move: Move, piece: ChessPiece): Boolean {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return true
        if (move.isForwardPawnMove(piece)) {
            val oneSquareAhead: Int
            val twoSquaresAhead: Int
            if (piece is WhitePawn) {
                oneSquareAhead = piecesCurrentIndex + ChessBoard.width
                twoSquaresAhead = piecesCurrentIndex + (ChessBoard.width * 2)
            } else {
                oneSquareAhead = piecesCurrentIndex - ChessBoard.width
                twoSquaresAhead = piecesCurrentIndex - (ChessBoard.width * 2)
            }
            val oneSquareAheadBlocksMove = board[oneSquareAhead] != null
            val twoSquaresAheadBlocksMove = move == Move.NORTHTWICE && board[twoSquaresAhead] != null
            return (oneSquareAheadBlocksMove || twoSquaresAheadBlocksMove)
        }
        return false
    }

    fun calculateMovesDestinationIndex(piecesCurrentIndex: Int, move: Move, distance: Int?): Int? {
        val destinationIndex = piecesCurrentIndex + move.changeOfPositionOnBoard(distance)
        return destinationIndex
    }

    fun doesMoveStayOnBoard(move: Move, startingIndex: Int, destinationIndex: Int): Boolean {
        if (destinationIndex < 0 || destinationIndex >= ChessBoard.totalSquares) {
            return false
        }
        val startingColumn: Column = getColumn(index = startingIndex) ?: return false
        val destinationColumn: Column = getColumn(index = destinationIndex) ?: return false
        val startingRow: Row = getRow(index = startingIndex) ?: return false
        val destinationRow: Row = getRow(index = destinationIndex) ?: return false
        val columnChange = startingColumn.number - destinationColumn.number
        val rowChange = startingRow.number - destinationRow.number

        return when (move) {
            Move.NORTH, Move.SOUTH, Move.RANGENORTH, Move.RANGESOUTH, Move.SOUTHTWICE, Move.NORTHTWICE -> {
                startingColumn == destinationColumn
            }
            Move.EAST, Move.WEST, Move.RANGEEAST, Move.RANGEWEST -> startingRow == destinationRow
            Move.NORTHWEST, Move.NORTHEAST, Move.SOUTHWEST, Move.SOUTHEAST -> {
                abs(startingColumn.number - destinationColumn.number) == 1 && abs(startingRow.number - destinationRow.number) == 1
            }
            Move.JUMPNNE, Move.JUMPNNW, Move.JUMPENE, Move.JUMPESE, Move.JUMPSSE, Move.JUMPSSW, Move.JUMPWNW, Move.JUMPWSW -> {
                (abs(columnChange) == 1 && abs(rowChange) == 2) || (abs(columnChange) == 2 && abs(rowChange) == 1)
            }
            Move.RANGENORTHEAST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn > startingColumn && destinationRow > startingRow
            }
            Move.RANGENORTHWEST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn < startingColumn && destinationRow > startingRow
            }
            Move.RANGESOUTHEAST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn > startingColumn && destinationRow < startingRow
            }
            Move.RANGESOUTHWEST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn < startingColumn && destinationRow < startingRow
            }
        }
    }

    fun getPiecesIndex(piece: ChessPiece): Int? {
        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.totalSquares) {
            if (board[index] == piece) {
                return index
            }
        }
        return null
    }

    fun getRow(index: Int): Row? {
        return Row.values().find { it.number == (floor(index.toDouble() / ChessBoard.width) + 1).toInt() }
    }

    fun getColumn(index: Int): Column? {
        return Column.values().find { it.number == index % ChessBoard.width }
    }

    fun getSquaresIndex(row: Row, column: Column): Int {
        for (index in column.indices()) {
            if (index in row.indices()) {
                return index
            }
        }
        return -1
    }

    companion object {
        val height = 8
        val width = 8
        val totalSquares = height * width
    }
}

enum class Row(val number: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8);

    fun endingIndex(): Int {
        return (this.number * ChessBoard.width) - 1
    }

    fun startingIndex(): Int {
        return (this.number - 1) * ChessBoard.width
    }

    fun indices(): Array<Int> {
        return (this.startingIndex()..this.endingIndex()).toList().toTypedArray()
    }
}

enum class Column(val number: Int) {
    A(1), B(2), C(3), D(4),
    E(5), F(6), G(7), H(8);

    fun indices(): Array<Int> {
        var indices: Array<Int> = Array(ChessBoard.height) { 0 }

        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.height) {
            indices[index] = (ChessBoard.width * index) + (this.number - 1)
        }
        return indices
    }
}

enum class Move {
    NORTH, SOUTH, EAST, WEST,
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
    RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST,
    JUMPNNE, JUMPNNW, JUMPENE, JUMPESE, JUMPSSE, JUMPSSW, JUMPWNW, JUMPWSW,
    NORTHTWICE, SOUTHTWICE;

    fun changeOfPositionOnBoard(distance: Int?): Int {
        return when (this) {
            NORTH -> ChessBoard.width
            SOUTH -> -ChessBoard.width
            NORTHTWICE -> ChessBoard.width * 2
            SOUTHTWICE -> -ChessBoard.width * 2
            EAST -> 1
            WEST -> -1
            NORTHWEST -> ChessBoard.width - 1
            NORTHEAST -> ChessBoard.width + 1
            SOUTHWEST -> -ChessBoard.width - 1
            SOUTHEAST -> -ChessBoard.width + 1
            JUMPNNE -> (ChessBoard.width * 2) + 1
            JUMPNNW -> (ChessBoard.width * 2) - 1
            JUMPENE -> ChessBoard.width + 2
            JUMPESE -> -ChessBoard.width + 2
            JUMPSSE -> -(ChessBoard.width * 2) + 1
            JUMPSSW -> -(ChessBoard.width * 2) - 1
            JUMPWNW -> ChessBoard.width - 2
            JUMPWSW -> -ChessBoard.width - 2
            RANGENORTH -> ChessBoard.width * (distance ?: 0)
            RANGESOUTH -> -ChessBoard.width * (distance ?: 0)
            RANGEEAST -> 1 * (distance ?: 0)
            RANGEWEST -> -1 * (distance ?: 0)
            RANGENORTHEAST -> (ChessBoard.width + 1) * (distance ?: 0)
            RANGENORTHWEST -> (ChessBoard.width - 1) * (distance ?: 0)
            RANGESOUTHEAST -> (-ChessBoard.width + 1) * (distance ?: 0)
            RANGESOUTHWEST -> (-ChessBoard.width - 1) * (distance ?: 0)
        }
    }

    fun isRange(): Boolean {
        return when (this) {
            RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
            RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST -> true
            else -> false
        }
    }

    fun isForwardPawnMove(piece: ChessPiece): Boolean {
        return (piece is Pawn && (this == SOUTHTWICE || this == NORTHTWICE || this == SOUTH || this == NORTH))
    }

    fun isDiagonalPawnMove(piece: ChessPiece): Boolean {
        return (piece is Pawn && (this == SOUTHEAST || this == SOUTHWEST || this == NORTHEAST || this == NORTHWEST))
    }
}

enum class ChessColor {
    BLACK, WHITE
}

sealed class ChessPiece() {
    abstract var moveSet: Array<Move>
    abstract var color: ChessColor
    override abstract fun toString(): String
}

class Knight(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.JUMPNNE, Move.JUMPNNW, Move.JUMPENE, Move.JUMPESE,
        Move.JUMPSSE, Move.JUMPSSW, Move.JUMPWNW, Move.JUMPWSW
    )
    override fun toString(): String {
        return "N "
    }
}

class Queen(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST,
        Move.RANGENORTHWEST, Move.RANGENORTHEAST, Move.RANGESOUTHWEST, Move.RANGESOUTHEAST
    )
    override fun toString(): String {
        return "Q "
    }
}

class King(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.SOUTH, Move.EAST, Move.WEST,
        Move.NORTHWEST, Move.NORTHEAST, Move.SOUTHWEST, Move.SOUTHEAST
    )
    override fun toString(): String {
        return "K "
    }
}

class Rook(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST
    )
    override fun toString(): String {
        return "R "
    }
}

class Bishop(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTHEAST, Move.RANGENORTHWEST, Move.RANGESOUTHEAST, Move.RANGESOUTHWEST
    )
    override fun toString(): String {
        return "B "
    }
}

sealed class Pawn(override var color: ChessColor): ChessPiece()

class BlackPawn(): Pawn(ChessColor.BLACK) {
    override var moveSet: Array<Move> = arrayOf(
        Move.SOUTH, Move.SOUTHEAST, Move.SOUTHWEST, Move.SOUTHTWICE
    )
    override fun toString(): String {
        return "X "
    }
}

class WhitePawn(): Pawn(ChessColor.WHITE) {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.NORTHEAST, Move.NORTHWEST, Move.NORTHTWICE
    )
    override fun toString(): String {
        return "O "
    }
}

