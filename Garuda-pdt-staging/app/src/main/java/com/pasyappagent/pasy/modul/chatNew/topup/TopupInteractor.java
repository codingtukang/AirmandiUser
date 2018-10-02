package com.pasyappagent.pasy.modul.chatNew.topup;

import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.response.VoucherResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface TopupInteractor {
    Observable<VoucherResponse> checkVoucher(String code, String amount);

    Observable<GTopup> topup(String amount, String voucherId, String method);
}
