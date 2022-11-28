package com.onwd.challenge.features.devicedetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.onwd.challenge.R
import com.onwd.challenge.devicedetails.presentation.DeviceDetailsFragment
import com.onwd.challenge.devicelist.model.toView
import com.onwd.challenge.devicelist.presentation.DeviceListFragment
import com.onwd.challenge.di.launchFragmentInHiltContainer
import com.onwd.challenge.mock.DeviceMock
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DeviceDetailsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_loading_devices_then_must_show_all_of_them() = runTest {
        launchFragmentInHiltContainer<DeviceDetailsFragment>(
            fragmentArgs = Bundle().also {  it.putParcelable("device", DeviceMock().toView()) }
        )
        Espresso.onView(withId(R.id.ivBattery)).check(matches(withDrawable(R.drawable.ic_baseline_battery_5_bar_24)))
        Espresso.onView(withId(R.id.tvBatteryLevel)).check(matches(withText("90%")))


        Espresso.onView(withId(R.id.ivStatus)).check(matches(withDrawable(R.drawable.ic_baseline_check_circle_24)))
        Espresso.onView(withId(R.id.tvStatus)).check(matches(withText("Ok")))


        Espresso.onView(withId(R.id.ivIcon)).check(matches(withDrawable(R.drawable.ic_baseline_laptop_24)))

        Espresso.onView(withId(R.id.tvDeviceName)).check(matches(withText("device-name")))
        Espresso.onView(withId(R.id.tvFirmwareVersion)).check(matches(withText("device-firmware")))


    }


}
fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
    }

    override fun matchesSafely(view: View): Boolean {
        val context = view.context
        val expectedBitmap = context.getDrawable(id)!!.toBitmap()

        return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
    }
}