package com.pasyappagent.pasy.modul.topup.topupmethod;

import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.response.TopupResponse;

import java.util.List;

/**
 * Created by Dhimas on 11/28/17.
 */

public interface PaymentMethodTopupView {
    void onSuccessRequest(TopupResponse response);

    void onSuccessRequestBanks(List<GBanks> listBank);
}
