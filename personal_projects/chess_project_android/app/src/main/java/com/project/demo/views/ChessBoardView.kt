package com.project.demo.views

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.project.demo.R
import com.project.demo.models.*
import kotlin.math.min

class ChessBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val whiteQueenBitmap = BitmapFactory.decodeResource(resources, R.drawable.whitequeen)
    val whiteKingBitmap = BitmapFactory.decodeResource(resources, R.drawable.whiteking)
    val blackQueenBitmap = BitmapFactory.decodeResource(resources, R.drawable.blackqueen)
    val blackKingBitmap = BitmapFactory.decodeResource(resources, R.drawable.blackking)
    var squareOrigins: ArrayList<Point> = arrayListOf()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val blackGreen = ContextCompat.getColor(context, R.color.colorBlackGreen)
        val whiteGreen = ContextCompat.getColor(context, R.color.colorWhiteGreen)

        val viewWidth = this.width.toFloat()
        val viewHeight = this.height.toFloat()
        val squareSize = (min(viewWidth, viewHeight) - 1) / 8

        squareOrigins.clear()
        for (row in 0..7) {
            for (column in 0..7) {
                val top: Float = squareSize * row
                val left: Float = squareSize * column
                squareOrigins.add(Point(x = left.toInt(), y = top.toInt()))
            }
        }

        canvas?.apply {
            for (row in 0..7) {
                for (column in 0..7) {
                    paint.color = if ((column + row) % 2 == 0) whiteGreen else blackGreen

                    val left: Float = squareSize * column
                    val right: Float = left + squareSize
                    val top: Float = squareSize * row
                    val bottom: Float = top + squareSize

                    drawRect(left, top, right, bottom, paint)
                }
            }

            // need starting positions to be driven by chessgame model, not initialized here
            var rect: Rect = makeRect(squareOrigins[Square(Row.ONE, Column.D)], squareSize)
            canvas?.drawBitmap(whiteQueenBitmap, null, rect, paint)
            rect = makeRect(squareOrigins[Square(Row.ONE, Column.E)], squareSize)
            canvas?.drawBitmap(whiteKingBitmap, null, rect, paint)

            rect = makeRect(squareOrigins[Square(Row.EIGHT, Column.D)], squareSize)
            canvas?.drawBitmap(blackQueenBitmap, null, rect, paint)
            rect = makeRect(squareOrigins[Square(Row.EIGHT, Column.E)], squareSize)
            canvas?.drawBitmap(blackKingBitmap, null, rect, paint)

        }
    }

    private fun makeRect(origin: Point, squareSize: Float): Rect {
        return Rect(origin.x, origin.y, origin.x + squareSize.toInt(), origin.y + squareSize.toInt())
    }
}


