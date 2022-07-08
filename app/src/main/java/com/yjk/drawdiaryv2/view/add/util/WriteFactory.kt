package com.yjk.drawdiaryv2.view.add.util

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.common.YLog

class WriteFactory {

    private val textSize = 20
    private var textViews: Array<TextView?> = arrayOfNulls<TextView>(20)

    /**
     * 디스플레이의 가로길이 반환
     * @param activity
     * @return
     */
    private fun getDisplayWidth(activity: Activity): Int {
        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        return outMetrics.widthPixels
    }

    /**
     * 글씨 적는 공간 ( 동적 생성 )
     * @param activity
     * @param column
     * @param count
     * @return
     */
    fun createWritePlace(activity: Activity, column: Int, count: Int): RelativeLayout? {
        val layout = RelativeLayout(activity)
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layout.layoutParams = layoutParams
        val typeface: Typeface? = ResourcesCompat.getFont(activity, R.font.misaeng)
        val size = getDisplayWidth(activity!!) / count
        textViews = arrayOfNulls<TextView>(count * column + 1)
        for (j in 0 until column) {
            for (i in 1..count) {
                val id = j * count + i
                val params = RelativeLayout.LayoutParams(size, size)
                val textView = TextView(activity)
                textView.id = id
                textView.gravity = Gravity.CENTER
                textView.text = ""
                textView.textSize = textSize.toFloat()
                textView.setTextColor(Color.BLACK)
                textView.setTypeface(typeface)
                if (i != 1) {
                    if(textViews[id-1] != null) {
                        params.addRule(RelativeLayout.RIGHT_OF, textViews[id - 1]!!.id)
                        textView.setBackgroundResource(R.drawable.shape_write_place2)
                    }
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    textView.setBackgroundResource(R.drawable.shape_write_place)
                }
                if (j != 0) {
                    if(textViews[id - count] != null) {
                        params.addRule(RelativeLayout.BELOW, textViews[id - count]!!.id)
                    }
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
                if (j > 0) {
                    params.setMargins(0, -10, 0, 0)
                }
                textView.layoutParams = params
                textViews[id] = textView
                layout.addView(textView)
            }
        }
        return layout
    }


    fun setString(text: String) {
        try {
            YLog.d(" text : $text")
            for (i in 1 until textViews.size - 1) {
                textViews[i]?.text = ""
            }
            for (i in 1..text.length) {
                textViews[i]?.text = "" + text[i - 1]
            }
        } catch (e: Exception) {
            YLog.d(" error : $e.message")
        }
    }
}