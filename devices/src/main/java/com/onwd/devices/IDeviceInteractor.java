package com.onwd.devices;

import io.reactivex.rxjava3.subjects.Subject;

public interface IDeviceInteractor {
    void startSearch(Subject<IDevice> observable);
}
