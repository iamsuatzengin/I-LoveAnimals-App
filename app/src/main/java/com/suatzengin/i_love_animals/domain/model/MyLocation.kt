package com.suatzengin.i_love_animals.domain.model

import com.google.android.gms.maps.model.LatLng

data class MyLocation(
    val address: String,
    val latLng: LatLng?
)