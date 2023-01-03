package com.suatzengin.i_love_animals.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentLoginBinding
import com.suatzengin.i_love_animals.util.UiEvent
import com.suatzengin.i_love_animals.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setStatusBarColor("#4F616E")
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
            findNavController().navigate(R.id.fromLoginToRegister)
        }
        observeLogin()
    }

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UiEvent.NavigateToHome -> {
                            findNavController().navigate(R.id.fromLoginToAdList)

                        }
                    }
                }
            }
            launch {
                viewModel.state.collectLatest { state ->
                    if (state.isLoading) binding.progressLinear.visibility = View.VISIBLE
                    else binding.progressLinear.visibility = View.INVISIBLE

                    if (state.message.isNotEmpty()) {
                        Snackbar.make(requireView(), state.message, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}
