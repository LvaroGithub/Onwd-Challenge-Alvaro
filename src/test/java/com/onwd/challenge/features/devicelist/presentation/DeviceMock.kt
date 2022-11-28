package com.onwd.challenge.features.devicelist.presentation

import com.onwd.devices.DeviceStatus
import com.onwd.devices.DeviceType.TYPE_LAPTOP
import com.onwd.devices.IDevice

data class DeviceMock(
    private val name: String = "device-name",
    private val firmware: String = "device-firmware",
    private val batteryLevel: Float = 0.9f,
    private val status: DeviceStatus = DeviceStatus.Ok,
    private val type: Int = TYPE_LAPTOP
    ): IDevice {


    override fun getName(): String {
        return name
    }

    override fun getFirmwareVersion(): String {
        return firmware
    }

    override fun getBatteryLevel(): Float {
        return batteryLevel
    }

    override fun getStatus(): DeviceStatus {
        return status
    }

    override fun getType(): Int {
        return type
    }

}
