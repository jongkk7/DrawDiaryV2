package com.yjk.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.yjk.draw.data.DrawConfig
import com.yjk.draw.data.DrawPoint
import com.yjk.draw.listener.DrawTouchListener
import com.yjk.draw.util.DrawHelper

/**
 * DrawView class
 *
 * TODO list --
 * - history 생성
 * - redo, undo 추가
 * - 지우개 추가
 * - paint 변경
 */
class DrawView :
    View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    val tag = "### ${javaClass.simpleName}"

    var config = DrawConfig()

    var canvas: Canvas? = null
    var oldBitmap: Bitmap? = null

    lateinit var currentPaint: Paint
    var currentPath: Path? = Path()

    val points = ArrayList<DrawPoint>()
    var finishPath = true

    init {
        initPaints()

        setOnTouchListener(DrawTouchListener(object : DrawTouchListener.IPointCallback {
            override fun onPoint(event: MotionEvent, x: Float, y: Float) {
                var point = DrawPoint()
                if ((event.action != MotionEvent.ACTION_UP) &&
                    (event.action != MotionEvent.ACTION_CANCEL)
                ) {
                    for (i in 0 until event.historySize) {
                        point = DrawPoint()
                        point.x = event.getHistoricalX(i)
                        point.y = event.getHistoricalY(i)
                        points.add(point)
                    }

                    point = DrawPoint()
                    point.x = event.x
                    point.y = event.y

                    finishPath = false
                } else {
                    finishPath = true
                }

                invalidate()
            }
        }))
    }

    private fun initPaints() {
        currentPaint = DrawHelper.createPaint()
        currentPaint.setColor(config.DEFAULT_COLOR)
        currentPaint.setAlpha(config.DEFAULT_ALPHA)
        currentPaint.setStrokeWidth(config.DEFAULT_PEN_SIZE)
        DrawHelper.setupStrokePaint(currentPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(currentPath == null){
            currentPath = Path()
        }else {
            currentPath?.rewind()
        }

        if (points.size == 1 || DrawHelper.isAPoint(points)) {
            canvas!!.drawCircle(
                points[0].x.toFloat(), points[0].y.toFloat(),
                currentPaint.strokeWidth / 2,
                createAndCopyColorAndAlphaForFillPaint(currentPaint, false)
            )
        } else if (points.size != 0) {

            var isFirst = true

            for (point in points) {
                if (isFirst) {
                    currentPath?.moveTo(point.x, point.y)
                    isFirst = false
                } else {
                    currentPath?.lineTo(point.x, point.y)
                }
            }

            if (currentPath != null) {
                canvas?.drawPath(currentPath!!, currentPaint)
            }
        }

    }


    private fun createAndCopyColorAndAlphaForFillPaint(from: Paint, copyWidth: Boolean): Paint {
        val paint = DrawHelper.createPaint()
        DrawHelper.setupFillPaint(paint)
        paint.color = from.color
        paint.alpha = from.alpha
        if (copyWidth) {
            paint.strokeWidth = from.strokeWidth
        }
        return paint
    }
}