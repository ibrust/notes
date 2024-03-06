package com.project.demo.models

enum class ChessColor {
    BLACK, WHITE;

    companion object {
        var playerColor: ChessColor = ChessColor.WHITE
    }
}