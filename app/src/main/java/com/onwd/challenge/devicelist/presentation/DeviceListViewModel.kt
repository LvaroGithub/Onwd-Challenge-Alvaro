package com.onwd.challenge.devicelist.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onwd.challenge.devicelist.model.DeviceView
import com.onwd.challenge.devicelist.model.toView
import com.onwd.devices.IDevice
import com.onwd.devices.IDeviceInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceListViewModel @Inject constructor(private val interactor: IDeviceInteractor): ViewModel() {

    sealed class UiState {
        object Loading: UiState()
        data class Content(
            val deviceList: List<DeviceView>
        ): UiState()
    }

    @VisibleForTesting
    val deviceList = mutableListOf<DeviceView>()
    private var selectedDevice: DeviceView? = null

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _navigateToDevice = MutableSharedFlow<DeviceView>()
    val navigateToDevice = _navigateToDevice.asSharedFlow()

    private val observable: Subject<IDevice> = PublishSubject.create()

    init {
        onSearch()
    }

    fun onSearch() = viewModelScope.launch (Dispatchers.Main.immediate) {
        deviceList.clear()
        observable
            .toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(io())
            .doOnNext {
                updateList(it)
            }
            .doOnComplete {
                finishSearch()
            }
            .observeOn(mainThread())
            .subscribe()
        interactor.startSearch(observable)
    }

    @VisibleForTesting
    fun updateList(device: IDevice) {
        _uiState.update {
            deviceList.add(device.toView())
            UiState.Content(deviceList.toList())
        }
    }

    private fun finishSearch() {
        if(deviceList.isEmpty()) {
            _uiState.update { UiState.Content(listOf()) }
        }
    }


    fun onDeviceSelected(device: DeviceView?) {
        selectedDevice = device
    }

    fun onOpenSelectedDevice() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            selectedDevice?.let {
                _navigateToDevice.emit(it)
            }
        }
    }

    fun onSearchClicked() {
        _uiState.update { UiState.Loading }
        onSearch()
    }

}