package com.suatzengin.i_love_animals.presentation.ad_list.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suatzengin.i_love_animals.databinding.AdListItemBinding
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.util.ClickListener

class AdListViewHolder(
    private val binding: AdListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(advertisement: Advertisement, onClickListener: ClickListener) {
        binding.apply {
            tvTitle.text = advertisement.title
            tvDescription.text = advertisement.description
            tvAddress.text = advertisement.location?.address

            binding.cardView.setOnClickListener {
                onClickListener.onClick(advertisement)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): AdListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdListItemBinding.inflate(layoutInflater)
            return AdListViewHolder(binding)
        }
    }
}