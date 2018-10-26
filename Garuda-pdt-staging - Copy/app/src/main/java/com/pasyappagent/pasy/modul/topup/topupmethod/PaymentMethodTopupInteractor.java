package com.pasyappagent.pasy.modul.topup.topupmethod;

import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.response.BankTransferResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 11/28/17.
 */

public interface PaymentMethodTopupInteractor {
    Observable<List<GBanks>> getBank();

    Observable<BankTransferResponse> topupConfirm(String orderId, String bankId, String accountId);

}
