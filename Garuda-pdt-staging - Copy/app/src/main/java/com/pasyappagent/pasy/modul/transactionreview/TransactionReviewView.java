package com.pasyappagent.pasy.modul.transactionreview;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.gson.GTransaction;
import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.component.network.response.QRTransactionResponse;

/**
 * Created by Dhimas on 12/7/17.
 */

public interface TransactionReviewView {
    void onSuccessGetBalance(String balance);

    void onSuccessRegisterPremium(MessageResponse mResponse);

    void onSuccessPayTransaction(GTransaction transaction);

    void onSuccessCheckReferral(String refferalId);

    void chargeAmount(String totalAmount, String fee);

    void charge(QRTransactionResponse json);
}
