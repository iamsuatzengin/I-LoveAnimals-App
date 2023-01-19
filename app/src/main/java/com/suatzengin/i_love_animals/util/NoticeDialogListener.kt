package com.suatzengin.i_love_animals.util

import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.Query

interface NoticeDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment, direction: Query.Direction)
    fun onDialogNegativeClick(dialog: DialogFragment)
}
