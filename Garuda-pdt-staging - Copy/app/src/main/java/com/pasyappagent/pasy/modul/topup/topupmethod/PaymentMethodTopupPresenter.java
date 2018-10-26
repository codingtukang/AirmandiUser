package com.pasyappagent.pasy.modul.topup.topupmethod;

/**
 * Created by Dhimas on 11/28/17.
 */

public interface PaymentMethodTopupPresenter {
    void requestBanks();

    void confirmPayment(String orderId, String bankId, String accountid);
}
