package com.suatzengin.i_love_animals.presentation.ad_list

import com.suatzengin.i_love_animals.domain.model.Advertisement


data class AdListState(
    val list: List<Advertisement> = emptyList(),
    val message: String = "",
    val isLoading: Boolean = false
)