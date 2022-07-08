package com.yjk.draw.data

import android.graphics.Color

class DrawConfig {

    val DEFAULT_PEN_SIZE = 4f
    val DEFAULT_COLOR = Color.parseColor("#FF000000")
    val DEFAULT_ALPHA = 255

    var paintColor = DEFAULT_COLOR
    var paintWidth = DEFAULT_PEN_SIZE
    var paintAlpha = DEFAULT_ALPHA

    companion object {
        val STYLE_PEN = 0
        val STYLE_CRAYON = 1
        val STYLE_ERASER = 2
    }
}