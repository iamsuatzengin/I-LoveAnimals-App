package com.suatzengin.i_love_animals.presentation.post_ad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.suatzengin.i_love_animals.databinding.FragmentPostAdSheetBinding
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.model.MyLocation
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
class PostAdSheetFragment(val myLocation: MyLocation?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPostAdSheetBinding


    private lateinit var auth: FirebaseAuth

    private val viewModel: PostAdViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostAdSheetBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filledTextField.editText?.setText(myLocation?.address ?: "")

        val title = binding.textFieldTitle.editText?.text
        val description = binding.textFieldDescription.editText?.text
        val phoneNumber = binding.textFieldNumber.editText?.text
        val date = Date(System.currentTimeMillis())

        binding.btnPostAd.setOnClickListener {
            val advertisement = Advertisement(
                title = title.toString(),
                description = description.toString(),
                location = myLocation!!,
                status = false,
                authorPhoneNumber = phoneNumber.toString(),
                authorEmail = auth.currentUser!!.email.toString(),
                date = Timestamp(date)
            )
            viewModel.postNewAd(advertisement = advertisement)
            clearTextFields()
        }

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as BottomSheetBehavior

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        println("STATE_DRAGGING")
                        behavior.setPeekHeight(200, true)
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
        observe()
    }

    private fun clearTextFields(){
        binding.apply {
            filledTextField.editText?.setText("")
            textFieldTitle.editText?.setText("")
            textFieldDescription.editText?.setText("")
            textFieldNumber.editText?.setText("")
        }
    }

    private fun observe(){
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when(event){
                    is UiEvent.ShowMessage -> {
                        Snackbar.make(
                            requireView(), event.message, Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}