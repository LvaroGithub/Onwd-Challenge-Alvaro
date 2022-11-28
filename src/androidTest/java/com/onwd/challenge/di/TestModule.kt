package com.onwd.challenge.di

import com.onwd.challenge.devicelist.di.DeviceListModule
import com.onwd.challenge.mock.FakeDeviceInteractor
import com.onwd.devices.DeviceInteractorStub
import com.onwd.devices.IDeviceInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [DeviceListModule::class]
)
class TestModule {
    @Provides
    fun deviceInteractorProvider(): IDeviceInteractor = FakeDeviceInteractor()

}