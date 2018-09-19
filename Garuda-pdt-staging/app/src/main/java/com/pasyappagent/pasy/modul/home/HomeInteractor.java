package com.pasyappagent.pasy.modul.home;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.gson.GBalance;

import rx.Observable;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface HomeInteractor {
    Observable<GBalance> getBalance();

    Observable<JsonObject> checkPremiumAgent();
}
