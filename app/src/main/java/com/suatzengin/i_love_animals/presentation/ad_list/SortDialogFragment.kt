package com.suatzengin.i_love_animals.presentation.ad_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.DialogSortBinding
import com.suatzengin.i_love_animals.util.NoticeDialogListener

class SortDialogFragment(
    private val listener: NoticeDialogListener<Direction>
) : DialogFragment() {

    private lateinit var binding: DialogSortBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSortBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            var selectedItem = dialogRg.checkedRadioButtonId
            dialogRg.setOnCheckedChangeListener { _, checkedId ->
                selectedItem = checkedId
            }
            btnCancel.setOnClickListener { listener.onDialogNegativeClick(this@SortDialogFragment) }
            btnOk.setOnClickListener {
                if (selectedItem == R.id.rb_descending) {
                    listener.onDialogPositiveClick(
                        dialog = this@SortDialogFragment,
                        query = Direction.DESCENDING
                    )
                } else {
                    listener.onDialogPositiveClick(
                        dialog = this@SortDialogFragment,
                        query = Direction.ASCENDING
                    )
                }
            }
        }
        return binding.root
    }
}
