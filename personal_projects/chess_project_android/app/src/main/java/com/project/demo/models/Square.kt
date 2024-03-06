package com.project.demo.models

import androidx.room.TypeConverter
import kotlin.math.floor

class Square(val row: Row, val column: Column) {
    override fun toString(): String {
        return "${column}${row}"
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

    private fun toIndex(): Int {
        return (row.number * ChessBoard.width) + column.number - 1
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
            return Square(unwrappedRow, unwrappedColumn)
        }
    }
}