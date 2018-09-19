package com.pasyappagent.pasy.modul.register.otp;

import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface OtpInteractor {
    Observable<AgentResponse> verifyAgent(String mobile, String code);

    Observable<MessageResponse> resendCode(String mobile);
}
