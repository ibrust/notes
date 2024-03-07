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
import com.project.demo.models.Bishop
import com.project.demo.models.BlackPawn
import com.project.demo.models.ChessBoard
import com.project.demo.models.ChessColor
import com.project.demo.models.ChessPiece
import com.project.demo.models.Column
import com.project.demo.models.GameState
import com.project.demo.models.King
import com.project.demo.models.Knight
import com.project.demo.models.Point
import com.project.demo.models.Queen
import com.project.demo.models.Rook
import com.project.demo.models.Row
import com.project.demo.models.Square
import com.project.demo.models.WhitePawn
import com.project.demo.models.get
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList
import kotlin.collections.MutableMap
import kotlin.collections.arrayListOf
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode
import kotlin.collections.mutableMapOf
import kotlin.collections.reversed
import kotlin.collections.set
import kotlin.math.max
import kotlin.math.min


interface ChessBoardViewDelegate {
    fun didTouchDownOnSquare(square: Square)
    fun didReleaseOnSquare(square: Square)
}

class ChessBoardView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var delegate: WeakReference<ChessBoardViewDelegate>? = null

    private var squareOrigins: ArrayList<Point> = arrayListOf()
    private val pieceBitmaps: MutableMap<Int, Bitmap> = mutableMapOf()
    private var state: State = State()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var movingPoint: Point? = null

    data class State(
        val chessBoard: Array<ChessPiece?> = arrayOfNulls(size = ChessBoard.height * ChessBoard.width),
        val fullMoveNumber: Int = 1,
        val colorToMove: ChessColor = ChessColor.WHITE,
        val activeSquare: Square? = null,
        val gameState: GameState = GameState.PLAYING
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            val comp1 = (chessBoard.contentEquals(other.chessBoard) && (fullMoveNumber == other.fullMoveNumber))
            val comp2 = (colorToMove == other.colorToMove) && (activeSquare == other.activeSquare)
            val comp3 = (gameState == other.gameState)

            return (comp1 && comp2 && comp3)
        }

        override fun hashCode(): Int {
            var result = chessBoard.contentHashCode()
            result = 31 * result + fullMoveNumber
            result = 17 * result + colorToMove.hashCode()
            result = 23 * result + (activeSquare?.hashCode() ?: 0)
            result = 47 * result + gameState.hashCode()
            return result
        }
    }

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredWidth = max(specWidth, minWidth)
        var desiredHeight: Int
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> desiredHeight = min(specHeight, max(specWidth, minHeight))
            else -> desiredHeight = max(specWidth, minHeight)
        }
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val viewWidth = w.toFloat()
        val viewHeight = h.toFloat()
        val squareSize = (min(viewWidth, viewHeight) - 1) / 8
        squareOrigins.clear()
        for (row in 0..7) {
            for (column in 0..7) {
                val top: Float = squareSize * row
                val left: Float = squareSize * column
                squareOrigins.add(Point(x = left.toInt(), y = top.toInt()))
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val blackGreen = ContextCompat.getColor(context, R.color.colorBlackGreen)
        val whiteGreen = ContextCompat.getColor(context, R.color.colorWhiteGreen)

        val viewWidth = this.width.toFloat()
        val viewHeight = this.height.toFloat()
        val squareSize = (min(viewWidth, viewHeight) - 1) / 8
        val movingPointCopy = movingPoint       // copying prevents screen tearing, not entirely sure why, value may be changing while drawing

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

            // for each piece gets its corresponding bitmap and render it in its current location
            for (square in Square.allSquares()) {
                val unwrappedPiece = state.chessBoard[square] ?: continue
                val piecesBitmap = pieceBitmaps[getChessPieceImageResource(unwrappedPiece)] ?: continue

                if (square != state.activeSquare || movingPointCopy == null) {
                    val rect: Rect = makeRect(squareOrigins[square], squareSize)
                    canvas.drawBitmap(piecesBitmap, null, rect, paint)
                }
            }
            // render the moving piece last so it shows up on top of other pieces
            if (movingPointCopy != null) {
                val activeSquare = state.activeSquare ?: return
                val unwrappedPiece = state.chessBoard[activeSquare] ?: return
                val piecesBitmap = pieceBitmaps[getChessPieceImageResource(unwrappedPiece)] ?: return
                // TODO: find a way to reuse the point object or something
                val topLeftPoint = Point(x = movingPointCopy.x - (squareSize / 2).toInt(), y = movingPointCopy.y - (squareSize / 2).toInt())
                val rect: Rect = makeRect(topLeftPoint, squareSize)
                canvas.drawBitmap(piecesBitmap, null, rect, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchedSquare = getSquareForTouchEvent(event.x.toInt(), event.y.toInt()) ?: return false
                delegate?.get()?.didTouchDownOnSquare(touchedSquare)
                movingPoint = null

                Log.d(TAG, "${event.x}:${event.y}")
            }
            MotionEvent.ACTION_MOVE -> {
                movingPoint = Point(event.x.toInt(), event.y.toInt())

                // TODO: invalidate only part of the screen
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val touchedSquare = getSquareForTouchEvent(event.x.toInt(), event.y.toInt()) ?: return false
                delegate?.get()?.didReleaseOnSquare(touchedSquare)

                Log.d(TAG, "${event.x}:${event.y}")
            }

        }
        return true
    }

    private fun getSquareForTouchEvent(x: Int, y: Int): Square? {
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

    private fun makeBitMap(piece: ChessPiece): Bitmap {
        val resourceFile = getChessPieceImageResource(piece)
        val bitmap = BitmapFactory.decodeResource(resources, resourceFile)
        pieceBitmaps[resourceFile] = bitmap
        return bitmap
    }

    private fun getChessPieceImageResource(piece: ChessPiece): Int {
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

    private fun makeRect(origin: Point, squareSize: Float): Rect {
        return Rect(origin.x, origin.y, origin.x + squareSize.toInt(), origin.y + squareSize.toInt())
    }

    companion object {
        private val TAG: String = "ChessBoardView"
    }
}


