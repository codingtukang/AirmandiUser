package com.pasyappagent.pasy.modul.sendbalance;

import com.pasyappagent.pasy.component.network.response.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface SendBalanceInteractor {
    Observable<MessageResponse> transferBalance(String mobile, String amount, String notes);
}
