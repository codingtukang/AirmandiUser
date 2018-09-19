package com.pasyappagent.pasy.modul.transactionreview;

import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/8/17.
 */

public interface TransactionReviewInteractor {
    Observable<MessageResponse> subscribePremium(String refferalId);

    Observable<TransactionResponse> payTransaction(String transactionId);
}
