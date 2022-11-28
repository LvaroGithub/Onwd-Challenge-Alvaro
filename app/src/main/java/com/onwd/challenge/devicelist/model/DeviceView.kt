package com.onwd.challenge.devicelist.model

import android.os.Parcelable
import com.onwd.challenge.R
import com.onwd.devices.DeviceStatus
import com.onwd.devices.DeviceType.TYPE_LAPTOP
import com.onwd.devices.DeviceType.TYPE_PHONE
import com.onwd.devices.DeviceType.TYPE_WATCH
import com.onwd.devices.IDevice
import kotlin.math.roundToInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceView (
    val iconRes: Int,
    val name: String,
    val firmwareVersion: String,
    val isOk: Boolean,
    val batteryResId: Int,
    val batteryLevel: Int
    ) : Parcelable

fun IDevice.toView(): DeviceView {

    val resId = when(type) {
        TYPE_PHONE -> R.drawable.ic_baseline_smartphone_24
        TYPE_WATCH -> R.drawable.ic_baseline_watch_24
        TYPE_LAPTOP -> R.drawable.ic_baseline_laptop_24
        else -> throw Exception("invalid type")
    }

    val batteryResId = when(batteryLevel) {
        in (0.0..0.2) -> R.drawable.ic_baseline_battery_1_bar_24
        in (0.2..0.4) -> R.drawable.ic_baseline_battery_2_bar_24
        in (0.4..0.6) -> R.drawable.ic_baseline_battery_3_bar_24
        in (0.6..0.8) -> R.drawable.ic_baseline_battery_4_bar_24
        in (0.8..1.0) -> R.drawable.ic_baseline_battery_5_bar_24
        else -> R.drawable.ic_baseline_battery_0_bar_24
    }

    return DeviceView(
        resId,
        name,
        firmwareVersion,
        status == DeviceStatus.Ok,
        batteryResId,
        (batteryLevel*100).roundToInt()
    )
}
