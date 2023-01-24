package com.suatzengin.i_love_animals.domain.model

import com.google.firebase.firestore.Exclude

data class User(
    val email: String = "",
    @get:Exclude val password: String = "",
    val fullName: String = "",
    val adCount: Int = 0,
    val completedAdCount: Int = 0
)
