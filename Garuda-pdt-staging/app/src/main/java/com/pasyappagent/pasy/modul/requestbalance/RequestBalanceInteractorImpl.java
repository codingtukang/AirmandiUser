package com.pasyappagent.pasy.modul.requestbalance;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/29/17.
 */

public class RequestBalanceInteractorImpl implements RequestBalanceInteractor {
    private NetworkService mService;

    public RequestBalanceInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> requestBalance(String mobile, String amount, String notes) {
        return mService.requestBalance(mobile, amount, notes).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
