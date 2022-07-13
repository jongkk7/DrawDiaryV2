package com.yjk.drawdiaryv2.view.main.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjk.drawdiaryv2.R
import com.yjk.drawdiaryv2.databinding.DialogDiarySettingBinding

/**
 * 공유, 삭제
 */
class DialogDiarySetting(val callback: IDiarySettingCallback) : BottomSheetDialogFragment() {

    private lateinit var mBinding: DialogDiarySettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        DialogDiarySettingBinding.inflate(inflater, container, false).apply {
            mBinding = this
        }.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mBinding.textViewClose.setOnClickListener {
            dismiss()
        }

        mBinding.textViewShare.setOnClickListener {
            callback.onShare()
            dismiss()
        }

        mBinding.textViewDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, w ->
                    callback.onDelete()
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, w ->
                    dialog.dismiss()
                }
                .create()
                .show()

            dismiss()
        }

    }

    interface IDiarySettingCallback{
        fun onDelete()
        fun onShare()
    }
}