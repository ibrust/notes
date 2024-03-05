package com.project.demo.models

data class Point(val x: Int, val y: Int)

operator fun ArrayList<Point>.get(square: Square): Point {
    val index: Int = this.getIndex(square)
    return this[index]
}

operator fun ArrayList<Point>.set(square: Square, point: Point) {
    this[this.getIndex(square)] = point
}

private fun ArrayList<Point>.getIndex(square: Square): Int {
    val rowNumber = square.row.number - 1
    val columnNumber = square.column.number - 1
    return ((7 - rowNumber) * 8) + columnNumber
}
