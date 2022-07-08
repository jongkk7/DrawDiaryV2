package com.yjk.drawdiaryv2.view.add

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.databinding.ActivityAddDiaryBinding
import com.yjk.drawdiaryv2.view.add.data.ColorData
import com.yjk.drawdiaryv2.view.add.util.DateFactory
import com.yjk.drawdiaryv2.view.add.util.WriteFactory

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
    private val message = ""
    private val writeFactory = WriteFactory()

    // 현재 선택된 색 , 기존의 색 ( 지우개 사용시 사용 )
    private val currentColor: String = ColorData.color10 // black;
    private val currentPen = 1
    private lateinit var colorButtons : Array<ImageView>
    private val PEN_STYLE = 1
    private val CRAYON_STYLE = 2
    private val ERASER_STYLE = 3
    private val COLOR_STYLE = 4

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

        setEvent()

        initWriteLayer()
        initWeatherLayer()
        initDateLayer()
        initBottomLayer()
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
    }

    fun initWriteLayer() {

        val writeLayer = writeFactory.createWritePlace(this, colume, row)
        mBinding.fragmentDiaryLayout.writeLayout.addView(writeLayer)
        mBinding.fragmentDiaryLayout.writeLayout.setOnClickListener {
            // TODO 글씨 넣는 팝업
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

        mBinding.fragmentBottomLayout.textSizeSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(i.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mBinding.fragmentBottomLayout.eraserLayout.eraserSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mBinding.fragmentDiaryLayout.drawview.setPenSize(i.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mBinding.fragmentBottomLayout.bottomPen1Button.setOnClickListener {

        }
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
        val colorData = ColorData()
//        mBinding.fragmentBottomLayout.bottomColorButton.
//        mBinding.setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color00)
//        )
//        colorButtons.get(1).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color01)
//        )
//        colorButtons.get(2).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color02)
//        )
//        colorButtons.get(3).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color03)
//        )
//        colorButtons.get(4).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color04)
//        )
//        colorButtons.get(5).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color05)
//        )
//        colorButtons.get(6).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color06)
//        )
//        colorButtons.get(7).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color07)
//        )
//        colorButtons.get(8).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color08)
//        )
//        colorButtons.get(9).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color09)
//        )
//        colorButtons.get(10).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color10)
//        )
//        colorButtons.get(11).setOnClickListener(
//            com.nainfox.drawview.view.add.AddDiaryActivity.ColorButtonClickListener(colorData.color11)
//        )
    }

    /**
     * 현재 선택된 팬에 따라 레이아웃 변경
     * 1: 연필
     * 2: 크래파스
     * 3: 지우개
     * 4: 컬러
     */
    private fun setLayoutToPen(pen : Int){
//        setDownAnim(currentPen)


    }

    override fun onBackPressed() {
        // todo 종료 전 팝업
        finish()
    }

}