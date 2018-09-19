package com.pasyappagent.pasy.modul.premium.agent;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;

/**
 * Created by Dhimas on 12/19/17.
 */

public interface AgentPremiumView {
    void onSuccessGetCashback(GCashback cashback);

    void onSuccessGetDownline(String downline);

    void onSuccessRedeem(GCashbackRedeem response);
}
