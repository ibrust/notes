package com.project.demo.models

sealed class ChessPiece() {
    abstract var moveSet: Array<Move>
    abstract var color: ChessColor
    abstract override fun toString(): String
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

    fun calculateDestinationSquare(currentSquare: Square, distance: Int?): Square? {
        return when (this) {
            NORTH -> currentSquare + Point(x = 0, y = 1)
            SOUTH -> currentSquare + Point(x = 0, y = -1)
            EAST -> currentSquare + Point(x = 1, y = 0)
            WEST -> currentSquare + Point(x = -1, y = 0)
            NORTHTWICE -> currentSquare + Point(x = 0, y = 2)
            SOUTHTWICE -> currentSquare + Point(x = 0, y = -2)
            NORTHWEST -> currentSquare + Point(x = -1, y = 1)
            NORTHEAST -> currentSquare + Point(x = 1, y = 1)
            SOUTHWEST -> currentSquare + Point(x = -1, y = -1)
            SOUTHEAST -> currentSquare + Point(x = 1, y = -1)
            JUMPNNE -> currentSquare + Point(x = 1, y = 2)
            JUMPNNW -> currentSquare + Point(x = -1, y = 2)
            JUMPENE -> currentSquare + Point(x = 2, y = 1)
            JUMPESE -> currentSquare + Point(x = 2, y = -1)
            JUMPSSE -> currentSquare + Point(x = 1, y = -2)
            JUMPSSW -> currentSquare + Point(x = -1, y = -2)
            JUMPWNW -> currentSquare + Point(x = -2, y = 1)
            JUMPWSW -> currentSquare + Point(x = -2, y = -1)
            RANGENORTH -> currentSquare + Point(x = 0, y = distance ?: 0)
            RANGESOUTH -> currentSquare + Point(x = 0, y = -(distance ?: 0))
            RANGEEAST -> currentSquare + Point(x = distance ?: 0, y = 0)
            RANGEWEST -> currentSquare + Point(x = -(distance ?: 0), y = 0)
            RANGENORTHEAST -> currentSquare + Point(x = distance ?: 0, y = distance ?: 0)
            RANGENORTHWEST -> currentSquare + Point(x = -(distance ?: 0), y = distance ?: 0)
            RANGESOUTHEAST -> currentSquare + Point(x = distance ?: 0, y = -(distance ?: 0))
            RANGESOUTHWEST -> currentSquare + Point(x = -(distance ?: 0), y = -(distance ?: 0))
        }
    }
}
