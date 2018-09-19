package com.pasyappagent.pasy.modul.register.otp;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/23/17.
 */

public class OtpInteractorImpl implements OtpInteractor {
    NetworkService networkService;

    public OtpInteractorImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public Observable<AgentResponse> verifyAgent(String mobile, String code) {
        return networkService.verifyAgent(mobile, code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MessageResponse> resendCode(String mobile) {
        return networkService.resendCode(mobile).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
