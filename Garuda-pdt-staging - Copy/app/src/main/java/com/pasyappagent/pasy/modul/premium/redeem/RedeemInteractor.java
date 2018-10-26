package com.pasyappagent.pasy.modul.premium.redeem;

import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 12/20/17.
 */

public interface RedeemInteractor {
    Observable<List<GReward>> getReward();

    Observable<GRedeem> redeemReward(String rewardId);
}
