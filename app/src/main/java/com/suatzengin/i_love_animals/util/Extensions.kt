package com.suatzengin.i_love_animals.util

import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.suatzengin.i_love_animals.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@BindingAdapter("android:setStatus")
fun setStatusIcon(view: ImageView, status: Boolean) {

    view.setImageResource(
        if (status) R.drawable.ic_check
        else R.drawable.close_circle_outline
    )
}

inline fun Fragment.observeFlows(crossinline observeFunction: suspend (CoroutineScope) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            observeFunction(this)
        }
    }
}

fun Fragment.checkLocationPermission(
    functionIfGranted: () -> Unit,
    functionIfDenied: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        functionIfGranted()
    } else {
        functionIfDenied()
    }
}
