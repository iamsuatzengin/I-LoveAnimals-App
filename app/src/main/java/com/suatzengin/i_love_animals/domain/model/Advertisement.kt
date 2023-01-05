package com.suatzengin.i_love_animals.domain.model

import com.google.firebase.Timestamp

data class Advertisement(
    val title: String? = null,
    val description: String? = null,
    val location: MyLocation? = null,
    val status: Boolean? = null,
    val authorPhoneNumber: String? = null,
    val authorEmail: String? = null,
    val date: Timestamp? = null
)
