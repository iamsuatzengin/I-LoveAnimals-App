package com.suatzengin.i_love_animals.presentation.ad_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.databinding.FragmentAdListBinding
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.model.Filter
import com.suatzengin.i_love_animals.presentation.ad_list.recycler_view.AdListRecyclerAdapter
import com.suatzengin.i_love_animals.util.ClickListener
import com.suatzengin.i_love_animals.util.NoticeDialogListener
import com.suatzengin.i_love_animals.util.observeFlows
import dagger.hilt.android.AndroidEntryPoint

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

        filterByStatus()
        observeAdListData()

        binding.filterBarLayout.btnSort.setOnClickListener {
            val dialog = SortDialogFragment(
                listener = object : NoticeDialogListener<Direction> {
                    override fun onDialogPositiveClick(
                        dialog: DialogFragment,
                        query: Direction
                    ) {
                        viewModel.setDirection(direction = query)
                        dialog.dismiss()
                    }

                    override fun onDialogNegativeClick(dialog: DialogFragment) {
                        dialog.dismiss()
                    }

                }
            )
            dialog.show(childFragmentManager, "SortDialog")
        }
        binding.filterBarLayout.btnFilter.setOnClickListener {
            val sheetFragment = FilterSheetFragment(
                listener = object : NoticeDialogListener<Filter> {
                    override fun onDialogPositiveClick(dialog: DialogFragment, query: Filter) {
                        viewModel.setFilter(filter = query)
                        dialog.dismiss()
                    }

                    override fun onDialogNegativeClick(dialog: DialogFragment) {
                        dialog.dismiss()
                    }
                }
            )
            sheetFragment.show(childFragmentManager, "FilterBottomSheet")
        }
    }

    private fun filterByStatus() {
        binding.apply {
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> viewModel.setStatus(status = false, selectedTabPosition = 0)
                        1 -> viewModel.setStatus(status = true, selectedTabPosition = 1)
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
        observeFlows {
            viewModel.state.collect { state ->
                adapter.setData(state.list)
                binding.tabLayout.getTabAt(state.selectedTabPosition)?.select()
            }
        }
    }
}
