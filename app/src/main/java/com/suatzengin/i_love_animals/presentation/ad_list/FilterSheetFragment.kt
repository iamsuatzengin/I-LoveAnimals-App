package com.suatzengin.i_love_animals.presentation.ad_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentFilterSheetBinding
import com.suatzengin.i_love_animals.domain.model.Filter
import com.suatzengin.i_love_animals.util.NoticeDialogListener


class FilterSheetFragment(
    private val listener: NoticeDialogListener<Filter>
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            var selected: Int? = null

            bottomRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                selected = checkedId
            }

            btnApply.setOnClickListener {
                if(selected == R.id.rb_all){
                    listener.onDialogPositiveClick(
                        dialog = this@FilterSheetFragment,
                        query = Filter.ALL
                    )
                }else{
                    listener.onDialogPositiveClick(
                        dialog = this@FilterSheetFragment,
                        query = Filter.MINE
                    )
                }
            }
            btnCancel.setOnClickListener {
                listener.onDialogNegativeClick(this@FilterSheetFragment)
            }
        }
    }

}