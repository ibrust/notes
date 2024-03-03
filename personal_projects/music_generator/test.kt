import kotlin.math.abs
import kotlin.math.floor

fun main() {
    println()
}

class ChessBoard() {

    var board: Array<ChessPiece?> = arrayOfNulls(ChessBoard.totalSquares)

    init {
        setupBoard()
    }

    fun setupBoard() {
        for (index in 0..<ChessBoard.totalSquares) {
            board[index] = null
        }

        for (index in Row.TWO.indices()) {
            board[index] = WhitePawn()
        }
        for (index in Row.SEVEN.indices()) {
            board[index] = BlackPawn()
        }

        board[getSquaresIndex(Row.ONE, Column.A)] = Rook(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.H)] = Rook(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.A)] = Rook(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.H)] = Rook(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.B)] = Bishop(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.G)] = Bishop(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.B)] = Bishop(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.G)] = Bishop(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.C)] = Knight(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.F)] = Knight(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.C)] = Knight(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.F)] = Knight(color = ChessColor.BLACK)

        board[getSquaresIndex(Row.ONE, Column.D)] = Queen(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.ONE, Column.E)] = King(color = ChessColor.WHITE)
        board[getSquaresIndex(Row.EIGHT, Column.D)] = Queen(color = ChessColor.BLACK)
        board[getSquaresIndex(Row.EIGHT, Column.E)] = King(color = ChessColor.BLACK)
    }

    fun calculateMovesDestination(piece: ChessPiece, move: Move, distance: Int?): Int? {
        val currentIndex = getPiecesIndex(piece) ?: return null
        val potentialDestinationIndex = currentIndex + move.changeOfPositionOnBoard(distance)

        if (potentialDestinationIndex < 0 || potentialDestinationIndex >= ChessBoard.totalSquares) {
            return null
        }
        if (!doesMoveStayOnBoard(move, startingIndex = currentIndex, destinationIndex = potentialDestinationIndex)) {
            return null
        }

        return potentialDestinationIndex
    }

    fun doesMoveStayOnBoard(move: Move, startingIndex: Int, destinationIndex: Int): Boolean {
        val startingColumn: Column = getColumn(index = startingIndex) ?: return false
        val destinationColumn: Column = getColumn(index = destinationIndex) ?: return false
        val startingRow: Row = getRow(index = startingIndex) ?: return false
        val destinationRow: Row = getRow(index = destinationIndex) ?: return false
        val columnChange = startingColumn.number - destinationColumn.number
        val rowChange = startingRow.number - destinationRow.number
        return when (move) {
            Move.NORTH, Move.SOUTH, Move.RANGENORTH, Move.RANGESOUTH -> startingColumn == destinationColumn
            Move.EAST, Move.WEST, Move.RANGEEAST, Move.RANGEWEST -> startingRow == destinationRow
            Move.NORTHWEST, Move.NORTHEAST, Move.SOUTHWEST, Move.SOUTHEAST -> {
                abs(startingColumn.number - destinationColumn.number) == 1 && abs(startingRow.number - destinationRow.number) == 1
            }
            Move.JUMPNNE, Move.JUMPNNW, Move.JUMPENE, Move.JUMPESE, Move.JUMPSSE, Move.JUMPSSW, Move.JUMPWNW, Move.JUMPWSW -> {
                (abs(columnChange) == 1 && abs(rowChange) == 2) || (abs(columnChange) == 2 && abs(rowChange) == 1)
            }
            Move.RANGENORTHEAST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn > startingColumn && destinationRow > startingRow
            }
            Move.RANGENORTHWEST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn < startingColumn && destinationRow > startingRow
            }
            Move.RANGESOUTHEAST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn > startingColumn && destinationRow < startingRow
            }
            Move.RANGESOUTHWEST -> {
                abs(columnChange) == abs(rowChange) && destinationColumn < startingColumn && destinationRow < startingRow
            }
        }
    }

    fun getPiecesIndex(piece: ChessPiece): Int? {
        for (index in 0..<ChessBoard.totalSquares) {
            if (board[index] == piece) {
                return index
            }
        }
        return null
    }

    fun getRow(index: Int): Row? {
        return Row.values().find { it.number == (floor(index.toDouble() / ChessBoard.width)).toInt() }
    }

    fun getColumn(index: Int): Column? {
        return Column.values().find { it.number == index % ChessBoard.width }
    }

    fun getSquaresIndex(row: Row, column: Column): Int {
        for (index in column.indices()) {
            if (index in row.indices()) {
                return index
            }
        }
        return -1
    }

    companion object {
        val height = 8
        val width = 8
        val totalSquares = height * width
    }
}

enum class Row(val number: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7), EIGHT(8);

    fun endingIndex(): Int {
        return (this.number * ChessBoard.width) - 1
    }

    fun startingIndex(): Int {
        return (this.number - 1) * ChessBoard.width
    }

    fun indices(): Array<Int> {
        return (this.startingIndex()..this.endingIndex()).toList().toTypedArray()
    }
}

enum class Column(val number: Int) {
    A(1), B(2), C(3), D(4),
    E(5), F(6), G(7), H(8);

    fun indices(): Array<Int> {
        var indices: Array<Int> = arrayOf(ChessBoard.height)

        for (index in 0..<ChessBoard.height) {
            indices[index] = (ChessBoard.width * index) + (this.number - 1)
        }
        return indices
    }
}

enum class Move {
    NORTH, SOUTH, EAST, WEST,
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    RANGENORTH, RANGESOUTH, RANGEEAST, RANGEWEST,
    RANGENORTHEAST, RANGENORTHWEST, RANGESOUTHEAST, RANGESOUTHWEST,
    JUMPNNE, JUMPNNW, JUMPENE, JUMPESE, JUMPSSE, JUMPSSW, JUMPWNW, JUMPWSW;

    fun changeOfPositionOnBoard(distance: Int?): Int {
        return when (this) {
            NORTH -> ChessBoard.width
            SOUTH -> -ChessBoard.width
            EAST -> 1
            WEST -> -1
            NORTHWEST -> ChessBoard.width - 1
            NORTHEAST -> ChessBoard.width + 1
            SOUTHWEST -> -ChessBoard.width - 1
            SOUTHEAST -> -ChessBoard.width + 1
            JUMPNNE -> (ChessBoard.width * 2) + 1
            JUMPNNW -> (ChessBoard.width * 2) - 1
            JUMPENE -> ChessBoard.width + 2
            JUMPESE -> -ChessBoard.width + 2
            JUMPSSE -> -(ChessBoard.width * 2) + 1
            JUMPSSW -> -(ChessBoard.width * 2) - 1
            JUMPWNW -> ChessBoard.width - 2
            JUMPWSW -> -ChessBoard.width - 2
            RANGENORTH -> ChessBoard.width * (distance ?: 0)
            RANGESOUTH -> -ChessBoard.width * (distance ?: 0)
            RANGEEAST -> 1 * (distance ?: 0)
            RANGEWEST -> -1 * (distance ?: 0)
            RANGENORTHEAST -> (ChessBoard.width + 1) * (distance ?: 0)
            RANGENORTHWEST -> (ChessBoard.width - 1) * (distance ?: 0)
            RANGESOUTHEAST -> (-ChessBoard.width + 1) * (distance ?: 0)
            RANGESOUTHWEST -> (-ChessBoard.width - 1) * (distance ?: 0)
        }
    }
}

enum class ChessColor {
    BLACK, WHITE
}

sealed class ChessPiece() {
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

sealed class Pawn(override var color: ChessColor): ChessPiece()

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

