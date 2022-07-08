package com.yjk.drawdiaryv2.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yjk.drawdiaryv2.databinding.ActivityMainBinding
import com.yjk.drawdiaryv2.view.add.ActivityAddDiary

class ActivityMain : AppCompatActivity() {

    private val RESULT_CODE_ADD = 9020

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var getAddResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setResultCallback()

        setEvent()

        loadList()
    }

    private fun setResultCallback(){
        getAddResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                // TODO 리스트 최신화
            }
        }
    }

    private fun setEvent(){

        mBinding.fragmentTitlebar.titlebarAddbutton.visibility = View.VISIBLE
        mBinding.fragmentTitlebar.titlebarAddbutton.setOnClickListener {
            val intent = Intent(this, ActivityAddDiary::class.java)
            getAddResult.launch(intent)
        }

    }

    private fun loadList(){

    }

}