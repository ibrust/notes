package com.project.demo.models

enum class ChessColor {
    BLACK, WHITE;

    fun otherColor(): ChessColor {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
        }
    }

    companion object {
        var playerColor: ChessColor = ChessColor.WHITE
    }
}