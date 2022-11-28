package com.onwd.challenge.devicelist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.onwd.challenge.R
import com.onwd.challenge.collectLatestLifecycleFlow
import com.onwd.challenge.collectLifecycleFlow
import com.onwd.challenge.databinding.FragmentDeviceListBinding
import com.onwd.challenge.devicelist.presentation.adapter.DeviceListAdapter
import com.onwd.challenge.devicelist.presentation.itemdecorator.LinearHorizontalSpacingDecoration
import com.onwd.challenge.devicelist.presentation.layoutmanager.ScaleLinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeviceListFragment: Fragment() {

    @VisibleForTesting
    val viewModel: DeviceListViewModel by viewModels()
    private val adapter by lazy {
        DeviceListAdapter().also {
            it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var snapHelper:PagerSnapHelper

    private var _binding: FragmentDeviceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = ScaleLinearLayoutManager(requireContext()) {
            viewModel.onDeviceSelected(adapter.currentList[it])
        }
        layoutManager.orientation = HORIZONTAL

        with(binding.rvDeviceList) {
            adapter = this@DeviceListFragment.adapter
            snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.rvDeviceList)
            layoutManager = this@DeviceListFragment.layoutManager
            clipToPadding = false
            snapHelper.attachToRecyclerView(this)
            itemAnimator = null
            addItemDecoration(LinearHorizontalSpacingDecoration(24))
        }


        collectLatestLifecycleFlow(viewModel?.uiState) {
            manageUiState(it)
        }
        collectLifecycleFlow(viewModel.navigateToDevice) {
            val action = DeviceListFragmentDirections.actionDeviceListFragmentToDeviceDetailsFragment(it)
            findNavController().navigate(action)
        }
        setListeners()
    }
    private fun manageUiState(state: DeviceListViewModel.UiState){
        when(state) {
            is DeviceListViewModel.UiState.Content ->  {
                adapter.submitList(state.deviceList)
                with(binding) {
                    tvDeviceCountLabel.text =
                        getString(R.string.device_count_label, state.deviceList.size.toString())
                    rvDeviceList.isVisible = true
                    tvDeviceCountLabel.isVisible = true
                    pbLoading.isVisible = false
                }
            }
            DeviceListViewModel.UiState.Loading -> {
                with(binding) {
                    tvDeviceCountLabel.isVisible = false
                    rvDeviceList.isVisible = false
                    pbLoading.isVisible = true
                }
            }
        }
    }

    private fun setListeners() {
        binding.buttonSearch.setOnClickListener {
            viewModel.onDeviceSelected(null)
            adapter.submitList(null)
            viewModel.onSearchClicked()
        }
        binding.buttonOpen.setOnClickListener {
            viewModel.onOpenSelectedDevice()
        }
    }
}




