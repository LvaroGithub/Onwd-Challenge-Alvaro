package com.onwd.devices;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
public @interface DeviceType {
    int TYPE_PHONE = 0;
    int TYPE_LAPTOP = 1;
    int TYPE_WATCH = 2;
}
