package com.pasyappagent.pasy.modul.topup.topupmethod;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.response.BankTransferResponse;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/28/17.
 */

public class PaymentMethodTopupInteractorImpl implements PaymentMethodTopupInteractor {
    private NetworkService mService;

    public PaymentMethodTopupInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<List<GBanks>> getBank() {
        return mService.getBanks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BankTransferResponse> topupConfirm(String orderId, String bankId, String accountId) {
        return mService.bankTransfer(orderId, bankId, accountId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
