package com.project.demo.models

enum class GameMode {
    EXPLORATION, COMPETITIVE;

    companion object {
        var currentGameMode: GameMode = EXPLORATION
    }
}
