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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentAdListBinding
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.presentation.ad_list.recycler_view.AdListRecyclerAdapter
import com.suatzengin.i_love_animals.util.ClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdListFragment : Fragment() {
    private lateinit var binding: FragmentAdListBinding
    private val viewModel: AdListViewModel by viewModels()
    private lateinit var adapter: AdListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdListBinding.inflate(inflater, container, false)
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllAd(true)

        filterByStatus()
        observeAdListData()
        binding.postAd.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                findNavController().navigate(R.id.mapsFragment)
            } else {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun filterByStatus() {
        binding.apply {
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> viewModel.getAllAd(status = true)
                        1 -> viewModel.getAllAd(status = false)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setupRecyclerView() {
        val recycleView = binding.rvAdList
        adapter = AdListRecyclerAdapter(onClickLister = object : ClickListener {
            override fun onClick(advertisement: Advertisement) {
                val action =
                    AdListFragmentDirections.actionAdListFragmentToAdDetailFragment(advertisement = advertisement)
                findNavController().navigate(action)
            }

        })
        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = adapter
    }

    private fun observeAdListData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.setData(state.list)
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
                findNavController().navigate(R.id.mapsFragment)
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
