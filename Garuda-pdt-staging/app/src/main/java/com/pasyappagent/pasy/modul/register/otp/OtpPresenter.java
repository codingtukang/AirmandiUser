package com.pasyappagent.pasy.modul.register.otp;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface OtpPresenter {
    void verifyAgent(String code, String mobile);

    void resendCode(String mobile);
}
