package com.suatzengin.i_love_animals.presentation.ad_list

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.DialogSortBinding
import com.suatzengin.i_love_animals.util.NoticeDialogListener

class SortDialogFragment(
    private val listener: NoticeDialogListener
) : DialogFragment() {

    private lateinit var binding: DialogSortBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSortBinding.inflate(layoutInflater)
        var selectedItem = binding.dialogRg.checkedRadioButtonId
        binding.dialogRg.setOnCheckedChangeListener { _, checkedId ->
            selectedItem = checkedId
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
            .setPositiveButton("Tamam") { _, _ ->
                if (selectedItem == R.id.rb_descending) {
                    listener.onDialogPositiveClick(dialog = this, direction = Direction.DESCENDING)
                } else {
                    listener.onDialogPositiveClick(dialog = this, direction = Direction.ASCENDING)
                }

            }
            .setNegativeButton("Iptal") { _, _ ->
                listener.onDialogNegativeClick(this)
            }

        return builder.create()
    }
}
