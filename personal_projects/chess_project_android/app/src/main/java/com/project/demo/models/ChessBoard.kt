package com.project.demo.models

import kotlin.math.abs
import kotlin.math.floor


class ChessBoard() {

    private var board: Array<ChessPiece?> = arrayOfNulls(ChessBoard.totalSquares)

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

    private fun doesMovePutKingInCheck(piece: ChessPiece, destinationIndex: Int): Boolean {
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

    private fun getCandidateMoveIndicesForPiece(piece: ChessPiece): MutableList<Int> {
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

    private fun isMovementPathValid(move: Move, piece: ChessPiece, destinationIndex: Int): Boolean {
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

    private fun isDiagonalPawnMoveIntoEmptySquare(move: Move, piece: ChessPiece, destinationIndex: Int): Boolean {
        val piecesCurrentIndex = getPiecesIndex(piece) ?: return true
        if (move.isDiagonalPawnMove(piece)) {
            return board[destinationIndex] == null
        }
        return false
    }

    private fun isForwardPawnMoveAndRunsIntoPieces(move: Move, piece: ChessPiece): Boolean {
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

    private fun calculateMovesDestinationIndex(piecesCurrentIndex: Int, move: Move, distance: Int?): Int? {
        val destinationIndex = piecesCurrentIndex + changeOfPositionOnBoard(move, distance)
        return destinationIndex
    }

    private fun doesMoveStayOnBoard(move: Move, startingIndex: Int, destinationIndex: Int): Boolean {
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

    private fun getPiecesIndex(piece: ChessPiece): Int? {
        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.totalSquares) {
            if (board[index] == piece) {
                return index
            }
        }
        return null
    }

    private fun getRow(index: Int): Row? {
        return Row.values().find { it.number == (floor(index.toDouble() / ChessBoard.width) + 1).toInt() }
    }

    private fun getColumn(index: Int): Column? {
        return Column.values().find { it.number == index % ChessBoard.width }
    }

    private fun getSquaresIndex(row: Row, column: Column): Int {
        for (index in column.indices()) {
            if (index in row.indices()) {
                return index
            }
        }
        return -1
    }

    fun changeOfPositionOnBoard(move: Move, distance: Int?): Int {
        return when (move) {
            Move.NORTH -> ChessBoard.width
            Move.SOUTH -> -ChessBoard.width
            Move.NORTHTWICE -> ChessBoard.width * 2
            Move.SOUTHTWICE -> -ChessBoard.width * 2
            Move.EAST -> 1
            Move.WEST -> -1
            Move.NORTHWEST -> ChessBoard.width - 1
            Move.NORTHEAST -> ChessBoard.width + 1
            Move.SOUTHWEST -> -ChessBoard.width - 1
            Move.SOUTHEAST -> -ChessBoard.width + 1
            Move.JUMPNNE -> (ChessBoard.width * 2) + 1
            Move.JUMPNNW -> (ChessBoard.width * 2) - 1
            Move.JUMPENE -> ChessBoard.width + 2
            Move.JUMPESE -> -ChessBoard.width + 2
            Move.JUMPSSE -> -(ChessBoard.width * 2) + 1
            Move.JUMPSSW -> -(ChessBoard.width * 2) - 1
            Move.JUMPWNW -> ChessBoard.width - 2
            Move.JUMPWSW -> -ChessBoard.width - 2
            Move.RANGENORTH -> ChessBoard.width * (distance ?: 0)
            Move.RANGESOUTH -> -ChessBoard.width * (distance ?: 0)
            Move.RANGEEAST -> 1 * (distance ?: 0)
            Move.RANGEWEST -> -1 * (distance ?: 0)
            Move.RANGENORTHEAST -> (ChessBoard.width + 1) * (distance ?: 0)
            Move.RANGENORTHWEST -> (ChessBoard.width - 1) * (distance ?: 0)
            Move.RANGESOUTHEAST -> (-ChessBoard.width + 1) * (distance ?: 0)
            Move.RANGESOUTHWEST -> (-ChessBoard.width - 1) * (distance ?: 0)
        }
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

    private fun endingIndex(): Int {
        return (this.number * ChessBoard.width) - 1
    }

    private fun startingIndex(): Int {
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
