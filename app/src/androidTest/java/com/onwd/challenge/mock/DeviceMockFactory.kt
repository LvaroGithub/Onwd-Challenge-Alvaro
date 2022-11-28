package com.onwd.challenge.mock

import com.onwd.devices.DeviceStatus
import com.onwd.devices.DeviceType
import com.onwd.devices.IDevice
import java.util.Random

internal object DeviceMockFactory {
    private val mFirmwareVersions = arrayOf(
        "1.2.4", "2.0.1", "1.9.2-RC1", "2.1.0a"
    )

    fun createRandomDevice(): DeviceMock {
        val random = Random()
        val deviceName = "Device-" + random.nextInt(5000)
        val firmwareVersion = mFirmwareVersions[random.nextInt(mFirmwareVersions.size)]
        return DeviceMock(
            deviceName,
            firmwareVersion,
            random.nextFloat(),
            getRandomDeviceStatus(random),
            getRandomDeviceType(random)
        )
    }

    private fun getRandomDeviceStatus(random: Random): DeviceStatus {
        return if (random.nextFloat() > 0.2f) DeviceStatus.Ok else DeviceStatus.Error
    }

    @DeviceType
    private fun getRandomDeviceType(random: Random): Int {
        return if (random.nextFloat() > 0.4) DeviceType.TYPE_LAPTOP else DeviceType.TYPE_PHONE
    }
}