package com.pasyappagent.pasy.modul.chatNew.topup;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface TopupPresenter {
    void checkVoucher(String voucher, String amount);

    void topup(String amount, String voucherId, String method);
}
