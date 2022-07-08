package com.yjk.drawdiaryv2.view.intro.tutorial.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjk.drawdiaryv2.common.ISingleCallback
import com.yjk.drawdiaryv2.databinding.ItemTutorialBinding
import com.yjk.drawdiaryv2.databinding.ItemTutorialLastBinding
import com.yjk.drawdiaryv2.view.intro.tutorial.data.TutorialData

class ViewPagerTutorial(
    val tutorialList: ArrayList<TutorialData>,
    val callback: ISingleCallback<Boolean>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_NOMAL = 0
    private val VIEW_TYPE_LAST = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            VIEW_TYPE_NOMAL -> {
                val binding =
                    ItemTutorialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemHolder(binding)
            }
            else -> {
                val binding =
                    ItemTutorialLastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LastItmeHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = VIEW_TYPE_NOMAL
        if (position == tutorialList.size - 1) {
            viewType = VIEW_TYPE_LAST
        }
        return viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = tutorialList[position]
        if (position == tutorialList.size - 1) {
            (holder as LastItmeHolder).bind(item)
            holder.bind(item)
        } else {
            (holder as ItemHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        return tutorialList.size
    }

    inner class ItemHolder(private val binding: ItemTutorialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TutorialData) {
            binding.tutorialImageview.setImageResource(item.res)
            binding.tutorialTextview.text = item.msg
        }
    }

    inner class LastItmeHolder(private val binding: ItemTutorialLastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TutorialData) {
            binding.tutorialTextview.text = item.msg
            binding.tutorialStartButton.setOnClickListener {
                callback.onCallback(true)
            }
        }
    }
}