package com.suatzengin.i_love_animals.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.databinding.FragmentLoginBinding
import com.suatzengin.i_love_animals.presentation.auth.AuthViewModel
import com.suatzengin.i_love_animals.util.UiEvent
import com.suatzengin.i_love_animals.util.observeFlows
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = binding.textFieldEmail.editText?.text
        val password = binding.textFieldPassword.editText?.text

        binding.btnLogin.setOnClickListener {
            viewModel.loginWithEmail(email = email.toString(), password = password.toString())
        }
        binding.btnToRegister.setOnClickListener {
            val action = LoginFragmentDirections.fromLoginToRegister()
            findNavController().navigate(action)
        }
        observeLogin()
    }

    private fun observeLogin() {
        observeFlows { scope ->
            scope.launch {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UiEvent.NavigateToHome -> {
                            val action = LoginFragmentDirections.fromLoginToAdList()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
            scope.launch {
                viewModel.stateLogin.collectLatest { state ->
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
