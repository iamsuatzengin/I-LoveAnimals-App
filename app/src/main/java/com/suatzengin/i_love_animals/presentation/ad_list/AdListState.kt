package com.suatzengin.i_love_animals.presentation.ad_list

import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.model.Filter


data class AdListState(
    val list: List<Advertisement> = emptyList(),
    val message: String = "",
    val isLoading: Boolean = false,
    val direction: Direction = Direction.DESCENDING,
    val status: Boolean = false,
    val selectedTabPosition: Int = 0,
    val filterByUser: Filter = Filter.ALL
)