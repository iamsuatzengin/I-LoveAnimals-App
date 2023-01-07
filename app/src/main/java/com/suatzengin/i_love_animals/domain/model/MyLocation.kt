package com.suatzengin.i_love_animals.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyLocation(
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
): Parcelable