package com.pasyappagent.pasy.modul.login.passcode;

import com.pasyappagent.pasy.component.network.response.AgentResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/24/17.
 */

public interface PasscodeLoginInteractor {
    Observable<AgentResponse> login(String mobile, String passcode, String imei);
}
