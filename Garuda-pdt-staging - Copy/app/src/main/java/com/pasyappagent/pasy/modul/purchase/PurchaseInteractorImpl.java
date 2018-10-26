package com.pasyappagent.pasy.modul.purchase;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.ServicesResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/14/17.
 */

public class PurchaseInteractorImpl implements PurchaseInteractor {
    private NetworkService mService;

    public PurchaseInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<ServicesResponse> getServices(String type, String amount, String no, String cat) {
        return mService.getService(type, amount, no, cat).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> setInquiry(String serviceId, String customerNo, String amount) {
        return mService.setInquiry(customerNo, serviceId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> getTransaction(String serviceId, String customerNo) {
        return mService.getTransactionWithoutInquiry(customerNo, serviceId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
