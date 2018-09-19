package com.pasyappagent.pasy.modul.topup.topup;

import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.gson.GVoucher;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface TopupView {
    void onSuccessRequest(GTopup topup);

    void onSuccessRequestVoucher(GVoucher voucher);
}
