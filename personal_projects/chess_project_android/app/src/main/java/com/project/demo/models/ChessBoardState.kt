package com.project.demo.models

class ChessBoardState(
    var moveWhiteCastledOn: Int?,
    var moveBlackCastledOn: Int?,
    var fullMoveNumber: Int,
    var colorToMove: ChessColor,
    var board: Array<ChessPiece?>
) {
    fun copy(): ChessBoardState {
        return ChessBoardState(
            moveWhiteCastledOn = this.moveWhiteCastledOn,
            moveBlackCastledOn = this.moveBlackCastledOn,
            fullMoveNumber = this.fullMoveNumber,
            colorToMove = this.colorToMove,
            board = this.board.copy()
        )
    }

    override fun toString(): String {
        return "${moveWhiteCastledOn}, ${moveBlackCastledOn}, ${fullMoveNumber}, ${colorToMove}, ${board}"
    }

    override fun hashCode(): Int {
        val sum1 = moveWhiteCastledOn.hashCode() * 31 + moveBlackCastledOn.hashCode() * 17
        val sum2 = fullMoveNumber.hashCode() * 37 + colorToMove.hashCode() * 29
        val sum3 = board.hashCode() * 11
        return sum1 + sum2 + sum3
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ChessBoardState) {
            return false
        }
        val comp1 = moveWhiteCastledOn == other.moveWhiteCastledOn && moveBlackCastledOn == other.moveBlackCastledOn
        val comp2 = fullMoveNumber == other.fullMoveNumber && colorToMove == other.colorToMove
        val comp3 = board == other.board
        return comp1 && comp2 && comp3
    }
}