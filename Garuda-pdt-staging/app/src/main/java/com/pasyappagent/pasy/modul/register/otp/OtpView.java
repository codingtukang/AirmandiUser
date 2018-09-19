package com.pasyappagent.pasy.modul.register.otp;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface OtpView {
    void onSuccessRequest();

    void onSuccessResendCode(String msg);
}
