package com.pasyappagent.pasy.modul.promo;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.DealsResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 4/26/18.
 */

public class DealsInteractorImpl implements DealsInteractor {
    private NetworkService mService;

    public DealsInteractorImpl(NetworkService mService) {
        this.mService = mService;
    }

    @Override
    public Observable<DealsResponse> getDeals() {
        return mService.getDeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<JsonObject> redeemDeal(String dealID) {
        return mService.redeem(dealID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
