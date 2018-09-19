package com.pasyappagent.pasy.modul.home.transaction;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.BankTransferResponse;
import com.pasyappagent.pasy.component.network.response.CashbackResponse;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionHistoryResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/21/17.
 */

public class TransactionInteractorImpl implements TransactionInteractor {
    private NetworkService mService;

    public TransactionInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<TransactionHistoryResponse> getTransaction(int page) {
        return mService.getTransaction(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionHistoryResponse> getTransactionPpob(int page) {
        return mService.getTransactionPPOB(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TopupResponse> getTopup(int page) {
        return mService.getTopupTransaction(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<CashbackResponse> getCashback(int page) {
        return mService.getCashbackList(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BankTransferResponse> updateBank(String orderId, String bankId, String accountId) {
        return mService.updateBank(orderId, bankId, accountId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
