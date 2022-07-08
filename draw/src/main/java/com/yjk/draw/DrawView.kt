package com.yjk.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.yjk.draw.data.DrawConfig
import com.yjk.draw.data.HistoryPath
import com.yjk.draw.listener.DrawTouchListener
import com.yjk.draw.util.DrawHelper
import java.util.*
import kotlin.collections.ArrayList

/**
 * DrawView class
 *
 * TODO list --
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

    val TAG = "### ${javaClass.simpleName}"

    var config = DrawConfig()

    var canvas: Canvas? = null
    var oldBitmap: Bitmap? = null

    lateinit var currentPaint: Paint
    var currentPath: Path? = Path()

    var finishPath = true
    var points = ArrayList<Point>()

    val paths = ArrayList<HistoryPath>()
    val cancelPathList = ArrayList<HistoryPath>()

    var isCleaning = false
    var mColor = Color.BLACK // 지우개로 변경 시 현재 컬러값 저장

    init {
        initPaints()

        setOnTouchListener(DrawTouchListener(object : DrawTouchListener.IPointCallback {
            override fun onPoint(event: MotionEvent, x: Float, y: Float) {
                var point = Point()
                if ((event.action != MotionEvent.ACTION_UP) &&
                    (event.action != MotionEvent.ACTION_CANCEL)
                ) {
                    for (i in 0 until event.historySize) {
                        point = Point()
                        point.x = event.getHistoricalX(i).toInt()
                        point.y = event.getHistoricalY(i).toInt()
                        points.add(point)
                    }

                    point = Point()
                    point.x = event.x.toInt()
                    point.y = event.y.toInt()

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

        if (paths.size == 0 && points.size == 0) return

        val finishedPath = finishPath
        finishPath = false

        for (currentPath in paths) {
            if (currentPath.isPoint) {
                canvas?.drawCircle(
                    currentPath.originX, currentPath.originX,
                    currentPath.paint.strokeWidth / 2, currentPath.paint
                )
            } else { // Else draw the complete path
                canvas?.drawPath(currentPath.path, currentPath.paint)
            }
        }

        if (currentPath == null) {
            currentPath = Path()
        } else {
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
                    currentPath?.moveTo(point.x.toFloat(), point.y.toFloat())
                    isFirst = false
                } else {
                    currentPath?.lineTo(point.x.toFloat(), point.y.toFloat())
                }
            }

            if (currentPath != null) {
                canvas?.drawPath(currentPath!!, currentPaint)
            }
        }

        if (finishedPath && points.size > 0) {
            createHistoryPathFromPoints()
        }
    }


    fun redo() {
        if (cancelPathList.size > 0) {
            paths.add(cancelPathList[cancelPathList.size - 1])
            cancelPathList.removeAt(cancelPathList.size - 1)
            invalidate()
        }
    }

    fun undo() {
        if (paths.size > 0) {
            finishPath = true

            cancelPathList.add(paths[paths.size - 1])
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    /**
     * clear
     * 화면을 모두 지우지만 history는 남겨둠 ( redo로 복구 가능 )
     */
    fun clear() {

        if (isCleaning) {
            return
        }

        Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    undo()
                    if (paths.size == 0) {
                        cancel()
                        isCleaning = false
                    }
                }
            }, 0, 50)
        }.start()

    }

    /**
     * 그림, 기록 모두 삭제
     */
    fun clearAll() {
        paths.clear()
        cancelPathList.clear()
        invalidate()
    }

    /**
     * paint 설정
     */
    fun setColor(@ColorInt color: Int) {
        config.paintColor = color
        setPaint()
    }

    fun setPenSize(size: Float) {
        config.paintWidth = size
        setPaint()
    }

    fun setAlpha(alpha: Int) {
        config.paintAlpha = alpha
        setPaint()
    }

    // 팬
    fun setPen(){
        config.paintColor = mColor
    }

    // 지우개
    fun setEraser(){
        mColor = config.paintColor
        config.paintColor = Color.WHITE
    }

    private fun setPaint() {
        currentPaint.setColor(config.paintColor)
        currentPaint.setAlpha(config.paintAlpha)
        currentPaint.setStrokeWidth(config.paintWidth)
        DrawHelper.setupStrokePaint(currentPaint)
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

    private fun createHistoryPathFromPoints() {
        var historyPath = HistoryPath(points, Paint(currentPaint))
        historyPath = HistoryPath(points, Paint(currentPaint))
//        when (paintStyle) {
//            1 -> historyPath = HistoryPath(points, Paint(currentPaint))
//            2 -> historyPath = HistoryPath(points, Paint(crayonPaint), paintStyle)
//            3 -> {
//            }
//        }
//        historyPath.paintStyle = 1
        paths.add(historyPath)
        points = java.util.ArrayList()
//        notifyPathDrawn()
//        notifyRedoUndoCountChanged()
    }
}