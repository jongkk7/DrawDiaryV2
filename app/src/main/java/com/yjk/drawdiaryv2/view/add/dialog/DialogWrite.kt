package com.yjk.drawdiaryv2.view.add.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.databinding.PopupWriteBinding

class DialogWrite(context: Context, val callback: ISingleCallback<String>) : Dialog(context) {

    private lateinit var mBinding: PopupWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = PopupWriteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setEvent()
    }

    private fun setEvent(){
        mBinding.addButton.setOnClickListener {
            if(mBinding.text.text.toString().isNotEmpty()){
                callback.onCallback(mBinding.text.text.toString())
                dismiss()
            }else {
                Toast.makeText(context, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        mBinding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}