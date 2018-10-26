package com.pasyappagent.pasy.modul.register;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/22/17.
 */

public class RegisterInteractorImpl implements RegisterInteractor {
    private NetworkService mService;

    public RegisterInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> register(String name, String email, String mobile, String refferalId) {
        return mService.register(name, mobile, email, refferalId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<AgentResponse> checkRefferal(String code) {
        return mService.checkRefferalCode(code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
