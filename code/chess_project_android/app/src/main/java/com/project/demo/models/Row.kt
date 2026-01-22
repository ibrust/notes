package com.project.demo.models

enum class Row(val number: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8);

    override fun toString(): String {
        return this.number.toString()
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