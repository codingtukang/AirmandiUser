package com.pasyappagent.pasy.modul.merchant.merchantlist;

import com.pasyappagent.pasy.component.network.gson.GMerchant;

import java.util.List;

/**
 * Created by Dhimas on 2/6/18.
 */

public interface MerchantListPresenter {
    void getMerchantList(String query);

    void loadNextTransactionPage();

    List<GMerchant> getMerchant();
}
