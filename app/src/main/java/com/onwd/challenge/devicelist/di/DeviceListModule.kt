package com.onwd.challenge.devicelist.di

import com.onwd.devices.DeviceInteractorStub
import com.onwd.devices.IDeviceInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DeviceListModule {

    @Provides
    fun deviceInteractorProvider(): IDeviceInteractor = DeviceInteractorStub()

}