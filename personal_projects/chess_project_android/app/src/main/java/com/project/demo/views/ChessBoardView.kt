package com.project.demo.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.project.demo.R
import com.project.demo.models.*
import java.lang.ref.WeakReference
import kotlin.math.min


interface ChessBoardViewDelegate {
    fun didTouchDownOnSquare(square: Square)
    fun didReleaseOnSquare(square: Square)
}

class ChessBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var delegate: WeakReference<ChessBoardViewDelegate>? = null

    var squareOrigins: ArrayList<Point> = arrayListOf()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val pieceBitmaps: MutableMap<Int, Bitmap> = mutableMapOf()
    private var state: State = State()

    data class State(
        val chessBoard: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.height * ChessBoard.width),
        val fullMoveNumber: Int = 1,
        val colorToMove: ChessColor = ChessColor.WHITE
    )

    init {
        val allPieces: ArrayList<ChessPiece> = arrayListOf(
            King(ChessColor.WHITE), King(ChessColor.BLACK), Queen(ChessColor.WHITE), Queen(ChessColor.BLACK),
            Bishop(ChessColor.WHITE), Bishop(ChessColor.BLACK), Rook(ChessColor.WHITE), Rook(ChessColor.BLACK),
            Knight(ChessColor.WHITE), Knight(ChessColor.BLACK), BlackPawn(), WhitePawn()
        )
        for (piece in allPieces) {
            makeBitMap(piece)
        }
    }

    fun update(state: State) {
        this.state = state
        this.invalidate()
    }

    fun makeBitMap(piece: ChessPiece): Bitmap {
        val resourceFile = getChessPieceImageResource(piece)
        val bitmap = BitmapFactory.decodeResource(resources, resourceFile)
        pieceBitmaps[resourceFile] = bitmap
        return bitmap
    }

    fun getChessPieceImageResource(piece: ChessPiece): Int {
        return when (piece) {
            is King -> if (piece.color == ChessColor.WHITE) R.drawable.whitekingresized else R.drawable.blackkingresized
            is Queen -> if (piece.color == ChessColor.WHITE) R.drawable.whitequeenresized else R.drawable.blackqueenresized
            is Bishop -> if (piece.color == ChessColor.WHITE) R.drawable.whitebishopresized else R.drawable.blackbishopresized
            is Knight -> if (piece.color == ChessColor.WHITE) R.drawable.whiteknightresized else R.drawable.blackknightresized
            is Rook -> if (piece.color == ChessColor.WHITE) R.drawable.whiterookresized else R.drawable.blackrookresized
            is WhitePawn -> R.drawable.whitepawnresized
            is BlackPawn -> R.drawable.blackpawnresized
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchedSquare = getSquareForTouchEvent(event.x.toInt(), event.y.toInt()) ?: return false
                delegate?.get()?.didTouchDownOnSquare(touchedSquare)
                Log.d(TAG, "${event.x}:${event.y}")
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                val touchedSquare = getSquareForTouchEvent(event.x.toInt(), event.y.toInt()) ?: return false
                delegate?.get()?.didReleaseOnSquare(touchedSquare)
                Log.d(TAG, "${event.x}:${event.y}")
            }

        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val blackGreen = ContextCompat.getColor(context, R.color.colorBlackGreen)
        val whiteGreen = ContextCompat.getColor(context, R.color.colorWhiteGreen)

        val viewWidth = this.width.toFloat()
        val viewHeight = this.height.toFloat()
        val squareSize = (min(viewWidth, viewHeight) - 1) / 8

        // TODO: - I'm not sure how often onDraw() fires, but if it's too often we may need to move this somewhere else
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
                val piecesBitmap = pieceBitmaps[getChessPieceImageResource(unwrappedPiece)] ?: continue
                val rect: Rect = makeRect(squareOrigins[square], squareSize)
                canvas.drawBitmap(piecesBitmap, null, rect, paint)
            }

        }
    }

    fun getSquareForTouchEvent(x: Int, y: Int): Square? {
        if (x < 0 || x > width || y < 0 || y > height) {
            return null
        }
        var targetRow: Row = Row.ONE
        for (row in Row.values().reversed()) {
            if (squareOrigins[Square(row, Column.A)].y > y) {
                break
            }
            targetRow = row
        }
        var targetColumn: Column = Column.A
        for (column in Column.values()) {
            if (squareOrigins[Square(Row.ONE, column)].x > x) {
                break
            }
            targetColumn = column
        }
        return Square(targetRow, targetColumn)
    }

    private fun makeRect(origin: Point, squareSize: Float): Rect {
        return Rect(origin.x, origin.y, origin.x + squareSize.toInt(), origin.y + squareSize.toInt())
    }

    companion object {
        val TAG: String = "ChessBoardView"
    }
}


