package com.suatzengin.i_love_animals.util

import com.suatzengin.i_love_animals.domain.model.Advertisement

interface ClickListener {
    fun onClick(advertisement: Advertisement)
}