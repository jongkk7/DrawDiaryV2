package com.yjk.drawdiaryv2.view.intro

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.common.CommonData
import com.yjk.drawdiaryv2.common.SharedData
import com.yjk.drawdiaryv2.databinding.ActivityLoadingBinding
import com.yjk.drawdiaryv2.databinding.ActivityMainBinding
import com.yjk.drawdiaryv2.view.intro.tutorial.ActivityTutorial
import com.yjk.drawdiaryv2.view.main.ActivityMain

class ActivityIntro : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoadingBinding

    var animationDrawable: AnimationDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setAnim()

        checkInfo()
    }


    private fun setAnim() {
        mBinding.loadingView.setBackgroundResource(R.drawable.anim_loading)
        animationDrawable = mBinding.loadingView.getBackground() as AnimationDrawable
    }

    private fun checkInfo(){

        if(SharedData(this).getBoolean(CommonData.IS_TUTORIAL_SKIP)){
            Handler().postDelayed({
                startActivity(Intent(this, ActivityMain::class.java))
                finish()
            }, 200)
        }else {
            Handler().postDelayed({
                startActivity(Intent(this, ActivityTutorial::class.java))
                finish()
            }, 2000)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            animationDrawable?.start()
        }else {
            animationDrawable?.stop()
        }
    }
}