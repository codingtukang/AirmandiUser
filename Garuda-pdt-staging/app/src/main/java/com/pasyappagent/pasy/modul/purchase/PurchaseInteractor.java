package com.pasyappagent.pasy.modul.purchase;

import com.pasyappagent.pasy.component.network.response.ServicesResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/14/17.
 */

public interface PurchaseInteractor {
    Observable<ServicesResponse> getServices(String type, String amount, String no, String cat);

    Observable<TransactionResponse> setInquiry(String serviceId, String customerNo, String amount);

    Observable<TransactionResponse> getTransaction(String serviceId, String customerNo);
}
