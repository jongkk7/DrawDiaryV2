package com.yjk.draw.data

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import com.yjk.draw.util.DrawHelper

class HistoryPath {

    var points = ArrayList<Point>()

    var paintStyle = 1
    var paintColor = 0
    var paintAlpha = 0
    var paintWidth = 0f

    var originX = 0f
    var originY = 0f

    var isPoint = false

    lateinit var path : Path
    lateinit var paint : Paint

    constructor(points: ArrayList<Point>, paint: Paint) {
        this.points = points
        this.paint = paint
        this.paintColor = paint.color
        this.paintAlpha = paint.alpha
        this.paintWidth = paint.strokeWidth
        this.originX = points[0].x.toFloat()
        this.originY = points[0].y.toFloat()
        this.isPoint = DrawHelper.isAPoint(points)

        generatePath()
        generatePaint()
    }

    // todo
    constructor(points: ArrayList<Point>, paint: Paint, style: Int) {
        this.points = points
    }

    fun generatePath() {
        path = Path()
        if (points != null) {
            var first = true
            for (i in points.indices) {
                val point: Point = points[i]
                if (first) {
                    path!!.moveTo(point.x.toFloat(), point.y.toFloat())
                    first = false
                } else {
                    path!!.lineTo(point.x.toFloat(), point.y.toFloat())
                }
            }
        }
    }

    private fun generatePaint() {
        paint = DrawHelper.createPaintAndInitialize(
            paintColor, paintAlpha, paintWidth,
            isPoint
        )
    }

}