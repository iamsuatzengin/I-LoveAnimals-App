package com.suatzengin.i_love_animals.util

import androidx.fragment.app.DialogFragment

interface NoticeDialogListener<in T> {
    fun onDialogPositiveClick(dialog: DialogFragment, query: T)
    fun onDialogNegativeClick(dialog: DialogFragment)
}
