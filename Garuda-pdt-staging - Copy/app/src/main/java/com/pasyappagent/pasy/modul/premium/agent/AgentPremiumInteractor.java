package com.pasyappagent.pasy.modul.premium.agent;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;

import rx.Observable;

/**
 * Created by Dhimas on 12/19/17.
 */

public interface AgentPremiumInteractor {
    Observable<GCashback> getCashback();

    Observable<JsonObject> getDownline();

    Observable<GCashbackRedeem> redeemCashback();
}
