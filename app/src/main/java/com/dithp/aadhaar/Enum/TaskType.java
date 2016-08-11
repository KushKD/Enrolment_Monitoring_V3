package com.dithp.aadhaar.Enum;

/**
 * Created by kuush on 8/11/2016.
 */
public enum  TaskType {
    USER_LOGIN(1),
    OTP_USER(2);

    int value; private TaskType(int value) { this.value = value; }
}
