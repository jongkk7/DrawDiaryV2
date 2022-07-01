package com.yjk.draw.listener

import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View

class DrawTouchListener(val callback: IPointCallback) : View.OnTouchListener {
    val tag = "###" + javaClass.simpleName

    var velocityTracker: VelocityTracker? = null

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker?.addMovement(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                Log.d(tag, "-------------------------------")
//                Log.d(tag, "좌표 ( ${event.x} , ${event.y} )")
                callback.onPoint(event, event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.computeCurrentVelocity(1)
                val veloX = velocityTracker?.xVelocity
                val veloY = velocityTracker?.yVelocity
//                Log.d(tag, "좌표 ( ${event.x}, ${event.y} )")
//                Log.d(tag, "------ $veloX ,$veloY")
                callback.onPoint(event, event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker?.recycle()
//                Log.d(tag, "좌표 ( ${event.x} , ${event.y} )")
//                Log.d(tag, "-------------------------------")
                callback.onPoint(event, event.x, event.y)
                velocityTracker = null
            }
        }

        return true
    }

    interface IPointCallback {
        fun onPoint(event: MotionEvent, x: Float, y: Float)
    }
}