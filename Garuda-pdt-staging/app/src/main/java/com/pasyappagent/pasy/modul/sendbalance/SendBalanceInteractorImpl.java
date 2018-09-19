package com.pasyappagent.pasy.modul.sendbalance;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/29/17.
 */

public class SendBalanceInteractorImpl implements SendBalanceInteractor {
    private NetworkService mService;

    public SendBalanceInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> transferBalance(String mobile, String amount, String notes) {
        return mService.transferBalance(mobile, amount, notes).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
