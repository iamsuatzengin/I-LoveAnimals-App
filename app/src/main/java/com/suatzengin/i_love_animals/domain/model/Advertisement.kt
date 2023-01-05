package com.suatzengin.i_love_animals.domain.model

import com.google.firebase.Timestamp

data class Advertisement(
    val title: String,
    val description: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String,
    val status: Boolean,
    val authorPhoneNumber: String? = null,
    val authorEmail: String,
    val date: Timestamp
)
