package com.pasyappagent.pasy.modul.requestbalance;

import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface RequestBalanceInteractor {
    Observable<MessageResponse> requestBalance(String destCustId, String amount, String notes);
}
