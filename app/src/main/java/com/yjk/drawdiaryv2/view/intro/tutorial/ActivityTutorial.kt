package com.yjk.drawdiaryv2.view.intro.tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.common.CommonData
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.common.SharedData
import com.yjk.drawdiaryv2.databinding.ActivityTutorialBinding
import com.yjk.drawdiaryv2.view.intro.tutorial.adapter.ViewPagerTutorial
import com.yjk.drawdiaryv2.view.intro.tutorial.data.TutorialData
import com.yjk.drawdiaryv2.view.main.ActivityMain

class ActivityTutorial : AppCompatActivity() {

    private lateinit var mBinding: ActivityTutorialBinding
    private val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initPager()

    }


    fun initPager(){

        // set tutorial data
        val list = ArrayList<TutorialData>()
        list.add(TutorialData(R.drawable.tutorial_image01, getString(R.string.tutorial01)))
        list.add(TutorialData(R.drawable.tutorial_image02, getString(R.string.tutorial02)))
        list.add(TutorialData(R.drawable.tutorial_image03, getString(R.string.tutorial03)))
        list.add(TutorialData(0, getString(R.string.tutorial03)))

        val adapter = ViewPagerTutorial(list, object :ISingleCallback<Boolean> {
            override fun onCallback(t: Boolean) {

                // 다음 화면
                SharedData(mContext).setBoolean(CommonData.IS_TUTORIAL_SKIP, true)
                startActivity(Intent(mContext, ActivityMain::class.java))
                finish()

            }
        })

        mBinding.viewpagerTuturial.adapter = adapter
    }

}