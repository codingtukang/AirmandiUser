package com.pasyappagent.pasy.modul.purchase;

/**
 * Created by Dhimas on 12/14/17.
 */

public interface PurchasePresenter {
    void getServices(String type, String amount, String no, String cat);

    void setInquiry(String serviceId, String customerNo, boolean usingInquiry, String amount);
}
