package com.suatzengin.i_love_animals.presentation.ad_list.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.util.ClickListener

class AdListRecyclerAdapter(private val onClickLister: ClickListener) : RecyclerView.Adapter<AdListViewHolder>() {

    private var oldAdList = emptyList<Advertisement>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdListViewHolder {
        return AdListViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: AdListViewHolder, position: Int) {
        holder.bind(oldAdList[position], onClickLister)
    }

    override fun getItemCount(): Int = oldAdList.size

    fun setData(newAdList: List<Advertisement>) {
        val diffUtil = RvDiffUtil(oldList = oldAdList, newList = newAdList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldAdList = newAdList
        diffResult.dispatchUpdatesTo(this)
    }

}
