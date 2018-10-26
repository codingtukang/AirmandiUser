package com.pasyappagent.pasy.modul.register;

import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/22/17.
 */

public interface RegisterInteractor {
    Observable<MessageResponse> register(String name, String email, String mobile, String refferalId);

    Observable<AgentResponse> checkRefferal(String code);
}
