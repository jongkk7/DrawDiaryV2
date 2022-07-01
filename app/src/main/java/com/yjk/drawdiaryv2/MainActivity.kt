package com.yjk.drawdiaryv2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yjk.draw.listener.DrawTouchListener
import com.yjk.drawdiaryv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        initDrawView()
    }

    private fun initDrawView(){
//        mBinding.drawView.setOnPath
    }


}