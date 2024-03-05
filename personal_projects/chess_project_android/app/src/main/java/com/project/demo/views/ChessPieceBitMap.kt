package com.project.demo.views

import android.graphics.Bitmap
import com.project.demo.models.ChessPiece

data class ChessPieceBitMap(val chessPiece: ChessPiece, val bitmap: Bitmap)

fun ArrayList<ChessPieceBitMap>.containsPiece(chessPiece: ChessPiece): Boolean {
    for (bitMap in this) {
        if (bitMap.chessPiece.pieceId == chessPiece.pieceId) {
            return true
        }
    }
    return false
}

fun ArrayList<ChessPieceBitMap>.getBitMap(pieceId: Int): ChessPieceBitMap? {
    for (bitMap in this) {
        if (bitMap.chessPiece.pieceId == pieceId) {
            return bitMap
        }
    }
    return null
}
