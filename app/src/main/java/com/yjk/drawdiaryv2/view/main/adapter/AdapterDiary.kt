package com.yjk.drawdiaryv2.view.main.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.databinding.ItemDiaryBinding
import com.yjk.drawdiaryv2.view.add.util.BitmapHelper
import com.yjk.drawdiaryv2.view.main.data.DiaryData

class AdapterDiary(val callback: ISingleCallback<DiaryData>) : ListAdapter<DiaryData, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<DiaryData>() {
        override fun areItemsTheSame(oldItem: DiaryData, newItem: DiaryData): Boolean =
            oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: DiaryData, newItem: DiaryData): Boolean =
            oldItem.time == newItem.time
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemDiaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.bind(getItem(position))
        }
    }

    inner class ItemViewHolder(val mBinding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: DiaryData) {

            // draw view
            val bitmapHelper = BitmapHelper()
            val drawBitmap = bitmapHelper.byteArrayToBitmap(item.all_url).copy(Bitmap.Config.ARGB_8888, true)
            mBinding.imageViewCanvas.setImageBitmap(drawBitmap)

            // title
            var title = item.message.trim()
            if(title.length >= 4){
                title = title.substring(0,4)
            }else {
                title = title.substring(0, title.length)
            }
            mBinding.textViewTitle.text = title

            // setting
            mBinding.buttonSetting.setOnClickListener {
                callback.onCallback(item)
            }
        }
    }
}