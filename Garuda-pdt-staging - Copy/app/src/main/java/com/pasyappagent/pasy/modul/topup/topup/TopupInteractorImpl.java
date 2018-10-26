package com.pasyappagent.pasy.modul.topup.topup;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.response.VoucherResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/27/17.
 */

public class TopupInteractorImpl implements TopupInteractor{
    private NetworkService mService;

    public TopupInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<VoucherResponse> checkVoucher(String code, String amount) {
        return mService.checkVoucher(code,amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GTopup> topup(String amount, String voucherId, String method) {
        return mService.topupBalance(amount, voucherId, method).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
