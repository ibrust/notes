package com.project.demo.models

import androidx.room.TypeConverter
import kotlin.math.floor

data class Point(val x: Int, val y: Int)

class Square(val row: Row, val column: Column) {
    override fun toString(): String {
        return row.toString() + column.toString()
    }

    fun toIndex(): Int {
        return (row.number * ChessBoard.width) + column.number - 1
    }

    operator fun plus(point: Point): Square? {
        val newRow = this.row + point.y
        val newColumn = this.column + point.x
        return makeSquare(newRow, newColumn)
    }

    operator fun minus(point: Point): Square? {
        val newRow = this.row - point.y
        val newColumn = this.column - point.x
        return makeSquare(newRow, newColumn)
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

        fun makeSquare(row: Row?, column: Column?): Square? {
            val unwrappedRow: Row = row ?: return null
            val unwrappedColumn: Column = column ?: return null
            return Square(row, column)
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

    operator fun plus(number: Int): Row? {
        val sum = number + this.number
        return if (sum > 8 || sum < 1) null else makeRow(sum)
    }

    operator fun minus(number: Int): Row? {
        val difference = this.number - number
        return if (difference > 8 || difference < 1) null else makeRow(difference)
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

    operator fun plus(number: Int): Column? {
        val sum = number + this.number
        return if (sum > 8 || sum < 1) null else Column.makeColumn(sum)
    }

    operator fun minus(number: Int): Column? {
        val difference = this.number - number
        return if (difference > 8 || difference < 1) null else Column.makeColumn(difference)
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