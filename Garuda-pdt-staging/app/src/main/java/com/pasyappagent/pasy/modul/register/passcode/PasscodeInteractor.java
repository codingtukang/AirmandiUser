package com.pasyappagent.pasy.modul.register.passcode;

import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface PasscodeInteractor {
    Observable<MessageResponse> setPasscode(String passcode, String confPasscode);
}
