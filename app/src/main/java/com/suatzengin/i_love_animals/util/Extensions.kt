package com.suatzengin.i_love_animals.util

import android.graphics.Color
import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Fragment.setStatusBarColor(color: String){
    val window = activity?.window
    window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window?.statusBarColor = Color.parseColor(color)
}