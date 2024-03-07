package com.project.demo.models

enum class ChessColor {
    BLACK, WHITE;

    fun otherColor(): ChessColor {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
        }
    }

    override fun toString(): String {
        return when (this) {
            BLACK -> "B"
            WHITE -> "W"
        }
    }

    companion object {
        var playerColor: ChessColor = ChessColor.WHITE
    }
}