package com.suatzengin.i_love_animals.presentation.post_ad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suatzengin.i_love_animals.databinding.FragmentPostAdSheetBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostAdSheetFragment(val address: String?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPostAdSheetBinding
    private val viewModel: PostAdViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostAdSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filledTextField.editText?.setText(address)
        binding.btnPostAd.setOnClickListener {
            dismiss()
        }

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as BottomSheetBehavior

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        println("STATE_DRAGGING")
                        behavior.setPeekHeight(200,true)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        println("STATE_EXPANDED")
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }
        behavior.addBottomSheetCallback(bottomSheetCallback)
    }

}