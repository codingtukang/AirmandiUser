package com.pasyappagent.pasy.modul.home.transaction;

import com.pasyappagent.pasy.component.network.response.BankTransferResponse;
import com.pasyappagent.pasy.component.network.response.CashbackResponse;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionHistoryResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/21/17.
 */

public interface TransactionInteractor {
    Observable<TransactionHistoryResponse> getTransaction(int page);

    Observable<TransactionHistoryResponse> getTransactionPpob(int page);

    Observable<TopupResponse> getTopup(int page);

    Observable<CashbackResponse> getCashback(int page);

    Observable<BankTransferResponse> updateBank(String orderId, String bankId, String accountId);
}
