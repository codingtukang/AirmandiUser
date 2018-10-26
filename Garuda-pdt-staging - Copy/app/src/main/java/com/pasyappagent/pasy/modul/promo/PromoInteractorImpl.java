package com.pasyappagent.pasy.modul.promo;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.network.response.PromoResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/18/18.
 */

public class PromoInteractorImpl implements PromoInteractor {
    private NetworkService mService;

    public PromoInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<PromoResponse> getList() {
        return mService.getPromo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GPromo> getPromoDetail(String promoId) {
        return mService.getPromoDetail(promoId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
