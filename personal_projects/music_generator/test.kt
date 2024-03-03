fun main() {
    println()
}

class ChessBoard() {
    val height = 8
    val width = 8
    val totalSquares = height * width

    var board: Array<ChessPiece?> = arrayOfNulls(totalSquares)

    fun setupBoard() {
        for (index in 0..<totalSquares) {
            board[index] = null
        }

        for (index in Row.TWO.startingIndex(width)..Row.TWO.endingIndex(width)) {
            board[index] = WhitePawn()
        }
        for (index in Row.SEVEN.startingIndex(width)..Row.SEVEN.endingIndex(width)) {
            board[index] = BlackPawn()
        }
    }
}

fun Row.startingIndex(boardWidth: Int): Int {
    return (this.number - 1) * boardWidth
}

fun Row.endingIndex(boardWidth: Int): Int {
    return (this.number * boardWidth) - 1
}

enum class Row(val number: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8)
}

enum class COLUMN(val number: Int) {
    A(1), B(2), C(3), D(4),
    E(5), F(6), G(7), H(8)
}

enum class Move {
    NORTH, SOUTH, EAST, WEST,
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
    RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST,
    JUMPNNE, JUMPNNW, JUMPENE, JUMPESE, JUMPSSE, JUMPSSW, JUMPWNW, JUMPWSW
}

enum class ChessColor {
    BLACK, WHITE
}

abstract class ChessPiece() {
    abstract var moveSet: Array<Move>
    abstract var color: ChessColor
}

class Knight(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.JUMPNNE, Move.JUMPNNW, Move.JUMPENE, Move.JUMPESE,
        Move.JUMPSSE, Move.JUMPSSW, Move.JUMPWNW, Move.JUMPWSW
    )
}

class Queen(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST,
        Move.RANGENORTHWEST, Move.RANGENORTHEAST, Move.RANGESOUTHWEST, Move.RANGESOUTHEAST
    )
}

class King(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.SOUTH, Move.EAST, Move.WEST,
        Move.NORTHWEST, Move.NORTHEAST, Move.SOUTHWEST, Move.SOUTHEAST
    )
}

class Rook(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTH, Move.RANGESOUTH, Move.RANGEEAST, Move.RANGEWEST
    )
}

class Bishop(override var color: ChessColor): ChessPiece() {
    override var moveSet: Array<Move> = arrayOf(
        Move.RANGENORTHEAST, Move.RANGENORTHWEST, Move.RANGESOUTHEAST, Move.RANGESOUTHWEST
    )
}

abstract class Pawn(override var color: ChessColor): ChessPiece()

class BlackPawn(): Pawn(ChessColor.BLACK) {
    override var moveSet: Array<Move> = arrayOf(
        Move.SOUTH, Move.SOUTHEAST, Move.SOUTHWEST
    )
}

class WhitePawn(): Pawn(ChessColor.WHITE) {
    override var moveSet: Array<Move> = arrayOf(
        Move.NORTH, Move.NORTHEAST, Move.NORTHWEST
    )
}

