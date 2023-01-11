package com.suatzengin.i_love_animals.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.suatzengin.i_love_animals.databinding.FragmentProfileBinding
import com.suatzengin.i_love_animals.presentation.IntroActivity
import com.suatzengin.i_love_animals.util.UiEvent
import com.suatzengin.i_love_animals.util.observeFlows
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvEmail.text = viewModel.currentUser?.email
            tvFullname.text = viewModel.currentUser?.displayName
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logOut()
        }

        observeFlows {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowMessage -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is UiEvent.LogOut -> {
                        val intent = Intent(requireContext(), IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
        }
    }
}