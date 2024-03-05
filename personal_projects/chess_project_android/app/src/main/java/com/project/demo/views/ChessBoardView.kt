package com.project.demo.views

import android.content.Context
import android.graphics.Bitmap
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

    var squareOrigins: ArrayList<Point> = arrayListOf()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    data class State(
        val chessBoard: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.height * ChessBoard.width),
        val fullMoveNumber: Int = 1,
        val colorToMove: ChessColor = ChessColor.WHITE
    )

    private var state: State = State()

    fun update(state: State) {
        this.state = state
        // create bitmaps for any newly added pieces
        for (piece in state.chessBoard) {
            val unwrappedPiece: ChessPiece = piece ?: continue
            if (pieceBitmaps.containsPiece(unwrappedPiece) == false) {
                pieceBitmaps.add(ChessPieceBitMap(
                    chessPiece = unwrappedPiece,
                    bitmap = BitmapFactory.decodeResource(
                        resources,
                        getChessPieceImageResource(unwrappedPiece)
                    )
                ))
            }
        }
        this.invalidate()
    }

    val pieceBitmaps: ArrayList<ChessPieceBitMap> = arrayListOf()

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

            for (square in Square.allSquares()) {
                // for each piece gets its corresponding bitmap and render it in its current location
                val unwrappedPiece = state.chessBoard[square] ?: continue
                val piecesBitmap = pieceBitmaps.getBitMap(unwrappedPiece.pieceId) ?: continue
                var rect: Rect = makeRect(squareOrigins[square], squareSize)
                canvas.drawBitmap(piecesBitmap.bitmap, null, rect, paint)
            }

        }
    }

    fun getChessPieceImageResource(piece: ChessPiece): Int {
        return when (piece) {
            is King -> if (piece.color == ChessColor.WHITE) R.drawable.whiteking else R.drawable.blackking
            is Queen -> if (piece.color == ChessColor.WHITE) R.drawable.whitequeen else R.drawable.blackqueen
            is Bishop -> if (piece.color == ChessColor.WHITE) R.drawable.whitebishop else R.drawable.blackbishop
            is Knight -> if (piece.color == ChessColor.WHITE) R.drawable.whiteknight else R.drawable.blackknight
            is Rook -> if (piece.color == ChessColor.WHITE) R.drawable.whiterook else R.drawable.blackrook
            is WhitePawn -> R.drawable.whitepawn
            is BlackPawn -> R.drawable.blackpawn
        }
    }

    private fun makeRect(origin: Point, squareSize: Float): Rect {
        return Rect(origin.x, origin.y, origin.x + squareSize.toInt(), origin.y + squareSize.toInt())
    }
}


