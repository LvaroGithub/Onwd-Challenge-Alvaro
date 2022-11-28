package com.onwd.challenge.devicedetails.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.onwd.challenge.R
import com.onwd.challenge.collectLatestLifecycleFlow
import com.onwd.challenge.databinding.FragmentDeviceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceDetailsFragment: Fragment() {

    private var _binding: FragmentDeviceDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DeviceDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDeviceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLatestLifecycleFlow(viewModel.uiState) {
            it?.let {
                with(binding) {
                    ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), it.deviceView.iconRes))
                    tvDeviceName.text = it.deviceView.name
                    tvFirmwareVersion.text = it.deviceView.firmwareVersion
                    if(it.deviceView.isOk) {
                        ivStatus.setImageDrawable(
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_circle_24)
                        )
                        tvStatus.text = getString(R.string.status_ok)
                    } else {
                        ivStatus.setImageDrawable(
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_error_24)
                        )
                        tvStatus.text = getString(R.string.status_error)
                    }
                    tvBatteryLevel.text = getString(R.string.battery_level_label, it.deviceView.batteryLevel.toString())
                    ivBattery.setImageDrawable(ContextCompat.getDrawable(requireContext(), it.deviceView.batteryResId))
                }

            }
        }
        viewModel.onStart()
    }
}