package com.project.demo.views

import android.content.Context
import android.util.AttributeSet
import android.view.View

class ChessBoardToolbar @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // what should the deisgn for this be?
    // I'd like it to be like chess.com
    // they have one bar above and one below the board
    // the bars have the player name, an icon, the piece count, and the score
    // I could add other things onto the top bar like a button to flip the board
}