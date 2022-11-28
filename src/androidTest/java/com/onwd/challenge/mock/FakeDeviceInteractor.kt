package com.onwd.challenge.mock

import com.onwd.devices.IDevice
import com.onwd.devices.IDeviceFoundListener
import com.onwd.devices.IDeviceInteractor
import io.reactivex.rxjava3.subjects.Subject

class FakeDeviceInteractor: IDeviceInteractor {
    private var deviceList = mutableListOf<DeviceMock>()

    fun getDeviceList(): List<DeviceMock>{
        return deviceList
    }

    override fun startSearch(observable: Subject<IDevice>?) {
        repeat(14){
            val device = DeviceMockFactory.createRandomDevice()
            observable?.onNext(device)
            deviceList.add(device)
        }
    }
}