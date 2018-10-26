package com.pasyappagent.pasy.modul.premium.redeem;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RedeemInteractorImpl implements RedeemInteractor {
    NetworkService mService;

    public RedeemInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<List<GReward>> getReward() {
        return mService.getReward().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GRedeem> redeemReward(String rewardId) {
        return mService.redeemReward(rewardId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
