package com.onwd.challenge.features.devicelist.presentation

import app.cash.turbine.test
import com.onwd.challenge.CoroutineTestRule
import com.onwd.challenge.devicelist.model.toView
import com.onwd.challenge.devicelist.presentation.DeviceListViewModel
import com.onwd.devices.IDeviceInteractor
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Callable
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeviceListViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    private lateinit var viewModel: DeviceListViewModel

    @Mock
    lateinit var deviceInteractorMock: IDeviceInteractor


    @Before
    fun setUp(){
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? ->
            Schedulers.trampoline()
        }
        viewModel = DeviceListViewModel(deviceInteractorMock)

    }

    @Test
    fun `WHEN onSearch called THEN return state must contain devices found`() = runTest {
        whenever(deviceInteractorMock.startSearch(any())).then {
            repeat(4) {
                viewModel.updateList(DeviceMock())
            }
        }
        viewModel.onSearch()
        val job = launch {
            viewModel.uiState.test {
                assertEquals(
                    DeviceListViewModel.UiState.Content(
                        listOf(DeviceMock().toView(), DeviceMock().toView(), DeviceMock().toView(), DeviceMock().toView())
                    ), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()

    }

    @Test
    fun `WHEN device opened THEN must navigate`() = runBlockingTest {
        viewModel.onDeviceSelected(DeviceMock().toView())

        val deferred = async {
            viewModel.navigateToDevice.first()
        }
        viewModel.onOpenSelectedDevice()

        assertEquals(
            DeviceMock().toView(), deferred.await()
        )

    }

}