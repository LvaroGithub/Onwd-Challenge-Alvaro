package com.onwd.challenge.devicedetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onwd.challenge.devicelist.model.DeviceView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DeviceDetailsViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    private val DEVICE_ARG_KEY  = "device"
    private val device = savedStateHandle.get<DeviceView>(DEVICE_ARG_KEY)

    data class UiContent(val deviceView: DeviceView)

    private val _uiState = MutableStateFlow<UiContent?>(null)
    val uiState = _uiState.asSharedFlow()

    fun onStart() = viewModelScope.launch {
        device?.let {
            _uiState.emit(UiContent(device))
        }
    }


}