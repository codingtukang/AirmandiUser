package com.pasyappagent.pasy.modul.promo;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.network.response.PromoResponse;

import rx.Observable;

/**
 * Created by Dhimas on 2/18/18.
 */

public interface PromoInteractor {
    Observable<PromoResponse> getList();

    Observable<GPromo> getPromoDetail(String promoId);
}
