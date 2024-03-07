package com.project.demo.models

import java.lang.StringBuilder

sealed class ChessPiece() {
    abstract var moveSet: Array<Move>
    abstract val color: ChessColor
    abstract val pieceId: Int
    var hasPieceMoved: Boolean = false

    abstract override fun toString(): String
    abstract override fun equals(other: Any?): Boolean
    abstract fun copy(): ChessPiece
    override fun hashCode(): Int {
        return pieceId.hashCode() * 31 + color.hashCode() * 17 + moveSet.hashCode() * 11
    }

    fun isPieceGrabbable(): Boolean {
        return when (GameMode.currentGameMode) {
            GameMode.EXPLORATION -> true
            GameMode.COMPETITIVE -> return ChessColor.playerColor == color
        }
    }

    companion object {
        private var id: Int = 0
        fun getPieceId(): Int {
            id += 1
            return id
        }
    }
}

class Knight(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.JUMPNNE, Move.JUMPNNW, Move.JUMPENE, Move.JUMPESE,
        Move.JUMPSSE, Move.JUMPSSW, Move.JUMPWNW, Move.JUMPWSW
    )

    override fun toString(): String {
        return "N$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Knight)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return Knight(color = this.color, pieceId = this.pieceId)
    }
}

class Queen(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST,
        Move.RANGENORTHWEST, Move.RANGENORTHEAST, Move.RANGESOUTHWEST, Move.RANGESOUTHEAST
    )
    override fun toString(): String {
        return "Q$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Queen)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return Queen(color = this.color, pieceId = this.pieceId)
    }
}

class King(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.SOUTH, Move.EAST, Move.WEST,
        Move.NORTHWEST, Move.NORTHEAST, Move.SOUTHWEST, Move.SOUTHEAST,
        Move.CASTLEKINGSIDE, Move.CASTLEQUEENSIDE
    )
    override fun toString(): String {
        return "K$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is King)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return King(color = this.color, pieceId = this.pieceId)
    }
}

class Rook(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST
    )
    override fun toString(): String {
        return "R$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Rook)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return Rook(color = this.color, pieceId = this.pieceId)
    }
}

class Bishop(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTHEAST, Move.RANGENORTHWEST, Move.RANGESOUTHEAST, Move.RANGESOUTHWEST
    )
    override fun toString(): String {
        return "B$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Bishop)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return Bishop(color = this.color, pieceId = this.pieceId)
    }
}

sealed class Pawn(override val color: ChessColor, override val pieceId: Int = getPieceId()): ChessPiece()

class BlackPawn(override val pieceId: Int = getPieceId()): Pawn(color = ChessColor.BLACK, pieceId = pieceId) {
    override var moveSet: Array<Move> = arrayOf(
        Move.SOUTH, Move.SOUTHEAST, Move.SOUTHWEST, Move.SOUTHTWICE, Move.ENPASSANT
    )
    override fun toString(): String {
        return "p$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is BlackPawn)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return BlackPawn(pieceId = this.pieceId)
    }
}

class WhitePawn(override val pieceId: Int = getPieceId()): Pawn(color = ChessColor.WHITE, pieceId = pieceId) {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.NORTHEAST, Move.NORTHWEST, Move.NORTHTWICE, Move.ENPASSANT
    )
    override fun toString(): String {
        return "P$color"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is WhitePawn)
            return false
        return color == other.color && pieceId == other.pieceId
    }

    override fun copy(): ChessPiece {
        return WhitePawn(pieceId = this.pieceId)
    }
}

fun Array<ChessPiece?>.copy(): Array<ChessPiece?> {
    val copyOfArray: Array<ChessPiece?> = arrayOfNulls(size = this.size)
    var index = 0
    for (element in this) {
        copyOfArray[index] = element?.copy()
        index++
    }
    return copyOfArray
}

fun Array<ChessPiece?>.hashString(): String {
    var builder = StringBuilder()
    for (square in Square.allSquares()) {
        val str: String = this[square].toString()
        builder.append(str)
    }
    return builder.toString()
}

fun Array<ChessPiece?>.getPiecesForColor(color: ChessColor): Array<ChessPiece> {
    val pieces: ArrayList<ChessPiece> = arrayListOf()
    for (piece in this) {
        piece ?: continue
        if (piece.color == color) {
            pieces.add(piece)
        }
    }
    return pieces.toTypedArray()
}

operator fun Array<ChessPiece?>.get(square: Square?): ChessPiece? {
    val unwrappedSquare: Square = square ?: return null
    for (index in unwrappedSquare.column.indices()) {
        if (index in unwrappedSquare.row.indices()) {
            return this[index]
        }
    }
    return null
}

operator fun Array<ChessPiece?>.set(square: Square?, piece: ChessPiece?) {
    val unwrappedSquare: Square = square ?: return
    for (index in unwrappedSquare.column.indices()) {
        if (index in unwrappedSquare.row.indices()) {
            this[index] = piece
        }
    }
}

private fun Column.indices(): Array<Int> {
    var indices: Array<Int> = Array(ChessBoard.height) { 0 }

    @OptIn(ExperimentalStdlibApi::class)
    for (index in 0..<ChessBoard.height) {
        indices[index] = (ChessBoard.width * index) + (this.number - 1)
    }
    return indices
}

private fun Row.endingIndex(): Int {
    return (this.number * ChessBoard.width) - 1
}

private fun Row.startingIndex(): Int {
    return (this.number - 1) * ChessBoard.width
}

private fun Row.indices(): Array<Int> {
    return (this.startingIndex()..this.endingIndex()).toList().toTypedArray()
}
