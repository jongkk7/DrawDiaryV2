package com.yjk.drawdiaryv2.view.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.common.YLog
import com.yjk.drawdiaryv2.databinding.ActivityMainBinding
import com.yjk.drawdiaryv2.view.add.ActivityAddDiary
import com.yjk.drawdiaryv2.view.main.adapter.AdapterDiary
import com.yjk.drawdiaryv2.view.main.data.DiaryData
import com.yjk.drawdiaryv2.view.main.dialog.DialogDiarySetting
import com.yjk.drawdiaryv2.view.main.presenter.MainPresenter

class ActivityMain : AppCompatActivity() {

    private val RESULT_CODE_ADD = 9020

    private val mContext = this
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var getAddResult: ActivityResultLauncher<Intent>

    private lateinit var mPresenter: MainPresenter
    private var adapterDiary: AdapterDiary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = MainPresenter()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setResultCallback()

        setEvent()
        loadList()
    }

    private fun setResultCallback() {
        getAddResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                loadList()
            }
        }
    }

    private fun setEvent() {

        mBinding.fragmentTitlebar.titlebarAddbutton.visibility = View.VISIBLE
        mBinding.fragmentTitlebar.titlebarAddbutton.setOnClickListener {
            val intent = Intent(this, ActivityAddDiary::class.java)
            getAddResult.launch(intent)
        }

    }

    private fun loadList() {

        val list = mPresenter.getDiaryList(this)

        mBinding.linearLayoutNoItemLayer.visibility =
            if (list.size == 0) View.VISIBLE else View.GONE

        adapterDiary = AdapterDiary(object : ISingleCallback<DiaryData> {
            override fun onCallback(res: DiaryData) {

                DialogDiarySetting(object : DialogDiarySetting.IDiarySettingCallback {
                    override fun onDelete() {
                        mPresenter.delete(mContext, res)
                        adapterDiary?.notifyItemRemoved(list.indexOf(res))
                        list.remove(res)
                    }

                    override fun onShare() {
                        mPresenter.share(mContext, res)
                    }

                }).show(supportFragmentManager, "setting")

            }
        })

        mBinding.diaryRecyclerview.layoutManager = LinearLayoutManager(this)
        mBinding.diaryRecyclerview.adapter = adapterDiary
        adapterDiary?.submitList(list)
    }

    override fun onResume() {
        super.onResume()
    }

}