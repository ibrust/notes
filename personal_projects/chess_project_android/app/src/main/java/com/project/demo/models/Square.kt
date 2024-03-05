package com.project.demo.models

import androidx.room.TypeConverter
import kotlin.math.floor

class Square(val row: Row, val column: Column) {
    override fun toString(): String {
        return row.toString() + column.toString()
    }

    fun toIndex(): Int {
        return (row.number * ChessBoard.width) + column.number - 1
    }

    class RoomConverter {
        @TypeConverter
        fun toSquare(index: Int): Square {
            return Square(
                row = Row.makeRow((floor(index.toDouble() / ChessBoard.width)).toInt()),
                column = Column.makeColumn(index % ChessBoard.width)
            )
        }

        @TypeConverter
        fun toIndex(square: Square): Int {
            return square.toIndex()
        }
    }

    companion object {
        fun allSquares(): ArrayList<Square> {
            val squares: ArrayList<Square> = arrayListOf()
            for (row in Row.values()) {
                for (column in Column.values()) {
                    squares.add(Square(row, column))
                }
            }
            return squares
        }
    }
}

enum class Row(val number: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8);

    override fun toString(): String {
        return this.number.toString()
    }

    private fun endingIndex(): Int {
        return (this.number * ChessBoard.width) - 1
    }

    private fun startingIndex(): Int {
        return (this.number - 1) * ChessBoard.width
    }

    fun indices(): Array<Int> {
        return (this.startingIndex()..this.endingIndex()).toList().toTypedArray()
    }

    fun squares(): ArrayList<Square> {
        val squares: ArrayList<Square> = arrayListOf()
        for (column in Column.values()) {
            squares.add(Square(this, column))
        }
        return squares
    }

    companion object {
        fun makeRow(number: Int): Row {
            return when (number % 8) {
                1 -> Row.ONE
                2 -> Row.TWO
                3 -> Row.THREE
                4 -> Row.FOUR
                5 -> Row.FIVE
                6 -> Row.SIX
                7 -> Row.SEVEN
                else -> Row.EIGHT
            }
        }
    }
}

enum class Column(val number: Int) {
    A(1), B(2), C(3), D(4),
    E(5), F(6), G(7), H(8);

    override fun toString(): String {
        return this.number.toString()
    }

    fun indices(): Array<Int> {
        var indices: Array<Int> = Array(ChessBoard.height) { 0 }

        @OptIn(ExperimentalStdlibApi::class)
        for (index in 0..<ChessBoard.height) {
            indices[index] = (ChessBoard.width * index) + (this.number - 1)
        }
        return indices
    }

    fun squares(): ArrayList<Square> {
        val squares: ArrayList<Square> = arrayListOf()
        for (row in Row.values()) {
            squares.add(Square(row, this))
        }
        return squares
    }

    companion object {
        fun makeColumn(number: Int): Column {
            return when (number % 8) {
                1 -> Column.A
                2 -> Column.B
                3 -> Column.C
                4 -> Column.D
                5 -> Column.E
                6 -> Column.F
                7 -> Column.G
                else -> Column.H
            }
        }
    }
}