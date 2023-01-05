package com.suatzengin.i_love_animals.presentation.ad_list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.databinding.FragmentAdListBinding
import com.suatzengin.i_love_animals.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdListFragment : Fragment() {
    private lateinit var binding: FragmentAdListBinding
    private val viewModel: AdListViewModel by viewModels()
    private val userViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postAd.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                findNavController().navigate(AdListFragmentDirections.fromAdListToMaps())
            } else {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        binding.btnLogout.setOnClickListener {
            userViewModel.signOut()
            val action = AdListFragmentDirections.fromAdListToLogin()
            findNavController().navigate(action)
        }
        observeAdListData()
    }

    private fun observeAdListData(){
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect{ state ->
                state.list.forEach {
                    println("ad => ${it.title}")
                }
            }
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        permission.entries.forEach {
            if (it.value) {
                //granted
                findNavController().navigate(AdListFragmentDirections.fromAdListToMaps())

            } else {
                // Permission is denied
                shouldShowRequestPermissionRationale(
                    it.key
                )
                Snackbar
                    .make(requireView(), "Ayarlardan izin ver", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}