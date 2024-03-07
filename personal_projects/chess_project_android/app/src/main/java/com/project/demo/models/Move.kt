package com.project.demo.models

enum class Move {
    NORTH, SOUTH, EAST, WEST,
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
    RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST,
    JUMPNNE, JUMPNNW, JUMPENE, JUMPESE, JUMPSSE, JUMPSSW, JUMPWNW, JUMPWSW,
    NORTHTWICE, SOUTHTWICE, ENPASSANT,
    CASTLEQUEENSIDE, CASTLEKINGSIDE;

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

    fun isCastling(): Boolean {
        return this == CASTLEKINGSIDE ||  this == CASTLEQUEENSIDE
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
            CASTLEKINGSIDE, CASTLEQUEENSIDE, ENPASSANT -> return null
        }
    }
}
