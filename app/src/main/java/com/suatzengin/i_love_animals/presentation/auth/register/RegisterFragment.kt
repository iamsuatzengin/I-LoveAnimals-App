package com.suatzengin.i_love_animals.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.databinding.FragmentRegisterBinding
import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.presentation.auth.AuthViewModel
import com.suatzengin.i_love_animals.util.UiEvent
import com.suatzengin.i_love_animals.util.observeFlows
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

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
        val firstName = binding.textFieldName.editText?.text
        val lastName = binding.textFieldSurname.editText?.text
        binding.btnRegister.setOnClickListener {
            val user = User(
                email = email.toString(), password = password.toString(),
                fullName = "$firstName $lastName", adCount = 0
            )
            viewModel.createUserWithEmail(user)
        }
        binding.tvHaveAccount.setOnClickListener { findNavController().popBackStack() }

        observeRegister()
    }

    private fun observeRegister() {
        observeFlows { scope ->
            scope.launch {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UiEvent.NavigateToLogin -> {
                            val action =
                                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            findNavController().navigate(action)
                        }
                        is UiEvent.ShowMessage -> {
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
            scope.launch {
                viewModel.stateRegister.collect { state ->
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