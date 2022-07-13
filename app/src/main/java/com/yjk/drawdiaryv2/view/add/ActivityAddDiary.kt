package com.yjk.drawdiaryv2.view.add

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.yjk.draw.DrawView
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.common.YLog
import com.yjk.drawdiaryv2.common.db.sql.SQLHelper
import com.yjk.drawdiaryv2.databinding.ActivityAddDiaryBinding
import com.yjk.drawdiaryv2.view.add.data.ColorData
import com.yjk.drawdiaryv2.view.add.dialog.DialogWrite
import com.yjk.drawdiaryv2.view.add.util.BitmapHelper
import com.yjk.drawdiaryv2.view.add.util.DateFactory
import com.yjk.drawdiaryv2.view.add.util.WriteFactory
import java.lang.Exception

class ActivityAddDiary : AppCompatActivity() {

    private lateinit var mBinding: ActivityAddDiaryBinding

    // 날씨
    private val WEATHER01 = 1
    private val WEATHER02 = 2
    private val WEATHER03 = 3
    private val WEATHER04 = 4
    private var weather = WEATHER01

    // 글씨 판
    private val row = 10
    private val colume = 2
    private var message = ""
    private val writeFactory = WriteFactory()

    // 현재 선택된 색 , 기존의 색 ( 지우개 사용시 사용 )
    private var currentColor: String = ColorData.color10 // black;
    private var currentPen = 1
    private lateinit var colorButtons: Array<ImageView>

    private lateinit var upAnim1: Animation
    private lateinit var upAnim2: Animation
    private lateinit var upAnim3: Animation
    private lateinit var downAnim1: Animation
    private lateinit var downAnim2: Animation
    private lateinit var downAnim3: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddDiaryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initWriteLayer()
        initWeatherLayer()
        initDateLayer()
        initBottomLayer()

        setEvent()

        startDownAnimation(DrawView.CRAYON_STYLE)
        startDownAnimation(DrawView.ERASER_STYLE)
        setLayoutToPen(DrawView.PEN_STYLE)
    }

    private fun setEvent() {

        mBinding.fragmentTitlebar.titlebarBackbutton.visibility = View.VISIBLE
        mBinding.fragmentTitlebar.titlebarBackbutton.setOnClickListener {
            onBackPressed()
        }

        mBinding.fragmentTitlebar.titlebarSavebutton.visibility = View.VISIBLE
        mBinding.fragmentTitlebar.titlebarSavebutton.setOnClickListener {
            mBinding.fragmentDiaryLayout.weatherLayout.root.visibility = View.INVISIBLE
            // todo 저장하기
        }

        mBinding.fragmentBottomLayout.bottomPen1Button.setOnClickListener {
            setLayoutToPen(DrawView.PEN_STYLE)
        }
        mBinding.fragmentBottomLayout.bottomPen2Button.setOnClickListener {
            setLayoutToPen(DrawView.CRAYON_STYLE)
        }
        mBinding.fragmentBottomLayout.bottomPen3Button.setOnClickListener {
            setLayoutToPen(DrawView.ERASER_STYLE)
        }

        mBinding.fragmentBottomLayout.bottomRedoButton.setOnClickListener {
            mBinding.fragmentDiaryLayout.drawview.redo()
        }
        mBinding.fragmentBottomLayout.bottomUndoButton.setOnClickListener {
            mBinding.fragmentDiaryLayout.drawview.undo()
        }

        mBinding.fragmentBottomLayout.bottomColorButton.setOnClickListener {
            setLayoutToPen(DrawView.COLOR_STYLE)
        }

        mBinding.fragmentBottomLayout.textSizeSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        mBinding.fragmentBottomLayout.eraserLayout.eraserSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mBinding.fragmentBottomLayout.eraserLayout.eraserAllButton.setOnClickListener {
            mBinding.fragmentDiaryLayout.drawview.clearAll()
        }

        mBinding.fragmentTitlebar.titlebarAddbutton.visibility = View.GONE
        mBinding.fragmentTitlebar.titlebarSavebutton.visibility = View.VISIBLE
        mBinding.fragmentTitlebar.titlebarSavebutton.setOnClickListener {
                saveDiary()
            }
    }

    fun initWriteLayer() {

        val writeLayer = writeFactory.createWritePlace(this, colume, row)
        mBinding.fragmentDiaryLayout.writeLayout.addView(writeLayer)
        mBinding.fragmentDiaryLayout.writeLayout.setOnClickListener {
            DialogWrite(this, object : ISingleCallback<String> {
                override fun onCallback(res: String) {
                    writeFactory.setString(res)
                    message = res
                }
            }).show()
        }

    }

    fun initDateLayer() {

        val dateFactory = DateFactory()
        val mYear = dateFactory.year
        val mMonth = dateFactory.month
        val mDay = dateFactory.day

        mBinding.fragmentDiaryLayout.dateLayout.dateYearTextview.text = "${dateFactory.year}"
        mBinding.fragmentDiaryLayout.dateLayout.dateMonthTextview.text = "${dateFactory.month + 1}"
        mBinding.fragmentDiaryLayout.dateLayout.dateDayTextview.text = "${dateFactory.day}"
        mBinding.fragmentDiaryLayout.dateLayout.dateDateTextview.text =
            "${dateFactory.getDayOfWeek()}"

        mBinding.fragmentDiaryLayout.dateLayout.root.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { datePicker, year, month, day ->
                    dateFactory.setCalendar(year, month, day)
                    mBinding.fragmentDiaryLayout.dateLayout.dateYearTextview.text = "$year"
                    mBinding.fragmentDiaryLayout.dateLayout.dateMonthTextview.text =
                        "${(month + 1)}"
                    mBinding.fragmentDiaryLayout.dateLayout.dateDayTextview.text = "$day"
                    mBinding.fragmentDiaryLayout.dateLayout.dateDateTextview.text =
                        dateFactory.getDayOfWeek()
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
    }

    fun initWeatherLayer() {
        mBinding.fragmentDiaryLayout.weatherTopLayer.weatherButton.setOnClickListener {
            mBinding.fragmentDiaryLayout.weatherLayout.root.visibility = View.VISIBLE
            mBinding.fragmentDiaryLayout.weatherLayout.root.z = 999f

            mBinding.fragmentDiaryLayout.weatherLayout.weatherButton01.setOnClickListener {
                selectWeather(WEATHER01)
            }
            mBinding.fragmentDiaryLayout.weatherLayout.weatherButton02.setOnClickListener {
                selectWeather(WEATHER02)
            }
            mBinding.fragmentDiaryLayout.weatherLayout.weatherButton03.setOnClickListener {
                selectWeather(WEATHER03)
            }
            mBinding.fragmentDiaryLayout.weatherLayout.weatherButton04.setOnClickListener {
                selectWeather(WEATHER04)
            }
        }
    }

    fun initBottomLayer() {

        // 애니메이션 초기화
        upAnim1 = AnimationUtils.loadAnimation(this, R.anim.anim_up)
        upAnim2 = AnimationUtils.loadAnimation(this, R.anim.anim_up)
        upAnim3 = AnimationUtils.loadAnimation(this, R.anim.anim_up)
        downAnim1 = AnimationUtils.loadAnimation(this, R.anim.anim_down)
        downAnim2 = AnimationUtils.loadAnimation(this, R.anim.anim_down)
        downAnim3 = AnimationUtils.loadAnimation(this, R.anim.anim_down)

//        mBinding.fragmentBottomLayout.textSizeSeekbar.thumb.colorFilter = ColorFilter()
//        mBinding.fragmentBottomLayout.eraserLayout.eraserSeekbar.thumb.colorFilter = ColorFilter()
        initColorButtons()

        mBinding.fragmentBottomLayout.textSizeSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(i.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mBinding.fragmentBottomLayout.eraserLayout.eraserSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(i.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    fun selectWeather(type: Int) {
        weather = WEATHER01
        when (type) {
            WEATHER01 -> {
                mBinding.fragmentDiaryLayout.weatherTopLayer.weatherButton.setBackgroundResource(R.drawable.draw_weather_snow_icon)
            }
            WEATHER02 -> {
                mBinding.fragmentDiaryLayout.weatherTopLayer.weatherButton.setBackgroundResource(R.drawable.draw_weather_cloud_icon)
            }
            WEATHER03 -> {
                mBinding.fragmentDiaryLayout.weatherTopLayer.weatherButton.setBackgroundResource(R.drawable.draw_weather_snow_icon)
            }
            WEATHER04 -> {
                mBinding.fragmentDiaryLayout.weatherTopLayer.weatherButton.setBackgroundResource(R.drawable.draw_weather_rain_icon)
            }
        }
        mBinding.fragmentDiaryLayout.weatherLayout.root.visibility = View.INVISIBLE
    }

    // 컬러 버튼 초기화
    private fun initColorButtons() {

        class ColorButtonClickListener(val color: String) : View.OnClickListener {
            override fun onClick(v: View?) {
                currentColor = color
                mBinding.fragmentDiaryLayout.drawview.setColor(Color.parseColor(color))
                mBinding.fragmentBottomLayout.bottomColorButton.setColorFilter(
                    Color.parseColor(
                        color
                    )
                )
            }
        }

        mBinding.fragmentBottomLayout.colorLayout.color00.setOnClickListener(
            ColorButtonClickListener(ColorData.color00)
        )
        mBinding.fragmentBottomLayout.colorLayout.color01.setOnClickListener(
            ColorButtonClickListener(ColorData.color01)
        )
        mBinding.fragmentBottomLayout.colorLayout.color02.setOnClickListener(
            ColorButtonClickListener(ColorData.color02)
        )
        mBinding.fragmentBottomLayout.colorLayout.color03.setOnClickListener(
            ColorButtonClickListener(ColorData.color03)
        )
        mBinding.fragmentBottomLayout.colorLayout.color04.setOnClickListener(
            ColorButtonClickListener(ColorData.color04)
        )
        mBinding.fragmentBottomLayout.colorLayout.color05.setOnClickListener(
            ColorButtonClickListener(ColorData.color05)
        )
        mBinding.fragmentBottomLayout.colorLayout.color06.setOnClickListener(
            ColorButtonClickListener(ColorData.color06)
        )
        mBinding.fragmentBottomLayout.colorLayout.color07.setOnClickListener(
            ColorButtonClickListener(ColorData.color07)
        )
        mBinding.fragmentBottomLayout.colorLayout.color08.setOnClickListener(
            ColorButtonClickListener(ColorData.color08)
        )
        mBinding.fragmentBottomLayout.colorLayout.color09.setOnClickListener(
            ColorButtonClickListener(ColorData.color09)
        )
        mBinding.fragmentBottomLayout.colorLayout.color10.setOnClickListener(
            ColorButtonClickListener(ColorData.color10)
        )
        mBinding.fragmentBottomLayout.colorLayout.color11.setOnClickListener(
            ColorButtonClickListener(ColorData.color11)
        )

    }

    /**
     * 현재 선택된 팬에 따라 레이아웃 변경
     * 1: 연필
     * 2: 크래파스
     * 3: 지우개
     * 4: 컬러
     */
    private fun setLayoutToPen(pen: Int) {

        if (currentPen == pen) return

        startDownAnimation(currentPen)
        startUpAnimation(pen)

        mBinding.fragmentBottomLayout.root.setBackgroundColor(Color.parseColor("#eeeeee"))
        mBinding.fragmentDiaryLayout.drawview.setPen(pen)
        currentPen = pen

        if (pen == DrawView.ERASER_STYLE) {
            mBinding.fragmentBottomLayout.bottomColorButton.setColorFilter(Color.parseColor("#FFFFFF"))
        } else {
            mBinding.fragmentBottomLayout.bottomColorButton.setColorFilter(
                Color.parseColor(
                    currentColor
                )
            )
        }

        mBinding.fragmentBottomLayout.colorLayout.root.visibility =
            if (pen == DrawView.COLOR_STYLE) View.VISIBLE else View.GONE
        mBinding.fragmentBottomLayout.textSizeSeekbar.visibility =
            if (pen == DrawView.PEN_STYLE || pen == DrawView.CRAYON_STYLE) View.VISIBLE else View.GONE
        mBinding.fragmentBottomLayout.eraserLayout.root.visibility =
            if (pen == DrawView.ERASER_STYLE) View.VISIBLE else View.GONE

    }

    private fun startDownAnimation(pen: Int) {
        when (pen) {
            DrawView.PEN_STYLE -> mBinding.fragmentBottomLayout.bottomPen1Button.startAnimation(
                downAnim1
            )
            DrawView.CRAYON_STYLE -> mBinding.fragmentBottomLayout.bottomPen2Button.startAnimation(
                downAnim2
            )
            DrawView.ERASER_STYLE -> mBinding.fragmentBottomLayout.bottomPen3Button.startAnimation(
                downAnim3
            )
        }
    }

    private fun startUpAnimation(pen: Int) {
        when (pen) {
            DrawView.PEN_STYLE -> mBinding.fragmentBottomLayout.bottomPen1Button.startAnimation(
                upAnim1
            )
            DrawView.CRAYON_STYLE -> mBinding.fragmentBottomLayout.bottomPen2Button.startAnimation(
                upAnim2
            )
            DrawView.ERASER_STYLE -> mBinding.fragmentBottomLayout.bottomPen3Button.startAnimation(
                upAnim3
            )
        }
    }

    override fun onBackPressed() {
        // todo 종료 전 팝업
        finish()
    }

    private fun saveDiary() {

        if (message.isNullOrEmpty()) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setMessage("저장하시겠습니까?")
            .setPositiveButton("저장") { dialog, which ->
                // 저장
                val bitmapHelper = BitmapHelper()

                val time = System.currentTimeMillis()
                var date =
                    mBinding.fragmentDiaryLayout.dateLayout.dateYearTextview.text.toString() + "_" +
                            mBinding.fragmentDiaryLayout.dateLayout.dateMonthTextview.text.toString() + "_" +
                            mBinding.fragmentDiaryLayout.dateLayout.dateDayTextview.text.toString() + "_" +
                            time

                val parentViewBitmap = bitmapHelper.viewToBitmap(mBinding.fragmentDiaryLayout.root)
                val childViewBitmap =
                    bitmapHelper.viewToBitmap(mBinding.fragmentDiaryLayout.drawview)
                val all_url = bitmapHelper.bitmapToByteArray(parentViewBitmap)
                val url = bitmapHelper.bitmapToByteArray(childViewBitmap)

                try {

                    val sqlHelper = SQLHelper(ActivityAddDiary@ this)
                    val result = sqlHelper.INSERT(date, weather, url, all_url, message)
                    if (result < 0) {
                        Toast.makeText(this, "저장에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }

                } catch (e: Exception) {
                    YLog.Companion.e(e.localizedMessage)
                }
            }
            .setNegativeButton("취소") { dialog, which ->
                // do nothing..
            }
            .show()
    }
}