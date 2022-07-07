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
        setEvent()
    }

    private fun initDrawView(){
//        mBinding.drawView.setOnPath
    }

    private fun setEvent(){

        mBinding?.redo?.setOnClickListener {
            mBinding?.drawView?.redo()
        }

        mBinding?.undo?.setOnClickListener {
            mBinding?.drawView?.undo()
        }
    }

}