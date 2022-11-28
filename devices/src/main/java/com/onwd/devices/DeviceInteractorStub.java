package com.onwd.devices;

import java.util.Random;

import io.reactivex.rxjava3.subjects.Subject;

public class DeviceInteractorStub implements IDeviceInteractor {

    @Override
    public void startSearch(Subject<IDevice> observable) {
        Random random = new Random();

        int numberOfDevicesToGenerate = random.nextInt(4);

        new Thread(() -> {

            try {
                Thread.sleep(random.nextInt(1000)); // Simulate search time
            } catch (InterruptedException ignored) {
            }

            if (observable != null) {

                for (int i = 0; i < numberOfDevicesToGenerate; i++) {
                    observable.onNext(DeviceFactory.createRandomDevice(random));
                }
                observable.onComplete();
            }

        }).start();
    }
}
