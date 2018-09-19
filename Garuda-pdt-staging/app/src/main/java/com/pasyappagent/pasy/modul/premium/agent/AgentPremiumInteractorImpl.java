package com.pasyappagent.pasy.modul.premium.agent;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/19/17.
 */

public class AgentPremiumInteractorImpl implements AgentPremiumInteractor{
    private NetworkService mService;

    public AgentPremiumInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<GCashback> getCashback() {
        return mService.getCashback().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<JsonObject> getDownline() {
        return mService.getTotalDownline().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GCashbackRedeem> redeemCashback() {
        return mService.redeemCashback().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
