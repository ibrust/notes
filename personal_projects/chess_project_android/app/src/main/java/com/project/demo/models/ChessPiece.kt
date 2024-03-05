package com.project.demo.models

sealed class ChessPiece() {
    abstract var moveSet: Array<Move>
    abstract var color: ChessColor
    abstract override fun toString(): String
}

operator fun Array<ChessPiece?>.get(square: Square): ChessPiece? {
    for (index in square.column.indices()) {
        if (index in square.row.indices()) {
            return this[index]
        }
    }
    return null
}

operator fun Array<ChessPiece?>.set(square: Square, piece: ChessPiece?) {
    for (index in square.column.indices()) {
        if (index in square.row.indices()) {
            this[index] = piece
        }
    }
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

enum class Move {
    NORTH, SOUTH, EAST, WEST,
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
    RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST,
    JUMPNNE, JUMPNNW, JUMPENE, JUMPESE, JUMPSSE, JUMPSSW, JUMPWNW, JUMPWSW,
    NORTHTWICE, SOUTHTWICE;

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
