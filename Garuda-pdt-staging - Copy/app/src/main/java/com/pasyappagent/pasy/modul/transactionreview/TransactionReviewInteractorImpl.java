package com.pasyappagent.pasy.modul.transactionreview;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/8/17.
 */

public class TransactionReviewInteractorImpl implements TransactionReviewInteractor {
    private NetworkService mService;

    public TransactionReviewInteractorImpl(NetworkService mService) {
        this.mService = mService;
    }

    @Override
    public Observable<MessageResponse> subscribePremium(String refferalId) {
        return mService.subscribePremium(refferalId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> payTransaction(String transactionId) {
        return mService.payTransaction(transactionId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }
}
