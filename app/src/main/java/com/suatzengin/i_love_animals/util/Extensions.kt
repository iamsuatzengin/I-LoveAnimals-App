package com.suatzengin.i_love_animals.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.suatzengin.i_love_animals.R

@BindingAdapter("android:setStatus")
fun setStatusIcon(view: ImageView, status: Boolean) {

    view.setImageResource(
        if (status) R.drawable.ic_check
        else R.drawable.close_circle_outline
    )
}
