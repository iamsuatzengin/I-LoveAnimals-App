package com.suatzengin.i_love_animals.presentation.ad_list.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.suatzengin.i_love_animals.domain.model.Advertisement


class RvDiffUtil(
    private val oldList: List<Advertisement>,
    private val newList: List<Advertisement>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}