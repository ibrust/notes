package com.project.demo.models

enum class Column(val number: Int) {
    A(1), B(2), C(3), D(4),
    E(5), F(6), G(7), H(8);

    override fun toString(): String {
        return this.number.toString()
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