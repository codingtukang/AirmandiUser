package com.pasyappagent.pasy.modul.creditcard.inputcreditcard;

import com.pasyappagent.pasy.component.network.gson.GCreditCard;

/**
 * Created by Dhimas on 2/4/18.
 */

public interface InputCreditcardView {
    void onSuccessTransactionMidtrans(GCreditCard gCreditCard);

    void setTotalAmount(String totalAmount);

    void onSuccessTransaction();
}
