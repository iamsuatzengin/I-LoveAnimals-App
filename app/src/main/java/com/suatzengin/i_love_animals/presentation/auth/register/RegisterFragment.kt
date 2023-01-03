package com.suatzengin.i_love_animals.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentRegisterBinding
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = binding.textFieldEmail.editText?.text
        val password = binding.textFieldPassword.editText?.text

        binding.btnRegister.setOnClickListener {
            viewModel.createUserWithEmail(email.toString(), password.toString())
        }
        binding.tvHaveAccount.setOnClickListener { findNavController().popBackStack() }

        observeRegister()
    }

    private fun observeRegister() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UiEvent.NavigateToLogin -> {
                            findNavController().navigate(R.id.fromRegisterToLogin)
                        }
                    }
                }
            }
            launch {
                viewModel.state.collect { state ->
                    if (state.isLoading) binding.progressLinear.visibility = View.VISIBLE
                    else binding.progressLinear.visibility = View.INVISIBLE

                    if (state.message.isNotEmpty()) {
                        Snackbar.make(requireView(), state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}