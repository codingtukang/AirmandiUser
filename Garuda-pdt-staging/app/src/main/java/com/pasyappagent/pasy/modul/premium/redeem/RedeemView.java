package com.pasyappagent.pasy.modul.premium.redeem;

import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;

import java.util.List;

/**
 * Created by Dhimas on 12/20/17.
 */

public interface RedeemView {
    void onSuccessGetReward(List<GReward> rewards);

    void onSuccessRedeemReward(GRedeem redeem);
}
