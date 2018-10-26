package com.pasyappagent.pasy.modul.purchase;

import com.pasyappagent.pasy.component.network.gson.GServices;
import com.pasyappagent.pasy.component.network.gson.GTransaction;

import java.util.List;

/**
 * Created by Dhimas on 12/14/17.
 */

public interface PurchaseView {
    void onSuccessGetService(List<GServices> listServices);

    void onSuccessInquiry(GTransaction transaction);
}
