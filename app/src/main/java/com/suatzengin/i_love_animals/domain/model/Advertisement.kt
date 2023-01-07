package com.suatzengin.i_love_animals.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Advertisement(
    var id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val location: MyLocation? = null,
    val status: Boolean = false,
    val authorPhoneNumber: String? = null,
    val authorEmail: String? = null,
    val date: Timestamp? = null
): Parcelable
