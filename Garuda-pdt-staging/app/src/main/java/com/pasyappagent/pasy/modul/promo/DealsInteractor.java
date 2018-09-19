package com.pasyappagent.pasy.modul.promo;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.response.DealsResponse;

import rx.Observable;

/**
 * Created by Dhimas on 4/26/18.
 */

public interface DealsInteractor {
    Observable<DealsResponse> getDeals();

    Observable<JsonObject> redeemDeal(String dealID);
}
