package com.suatzengin.i_love_animals.presentation.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.databinding.FragmentChangePasswordBinding
import com.suatzengin.i_love_animals.util.UiEvent
import com.suatzengin.i_love_animals.util.observeFlows
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        val newPassword = binding.textFieldNewPassword.editText?.text
        binding.btnChangePassword.setOnClickListener {
            viewModel.changePassword(newPassword = newPassword.toString())
        }
    }

    private fun observe() {
        observeFlows {
            viewModel.eventFlow.collectLatest { event ->
                if(event is UiEvent.ShowMessage){
                    Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}