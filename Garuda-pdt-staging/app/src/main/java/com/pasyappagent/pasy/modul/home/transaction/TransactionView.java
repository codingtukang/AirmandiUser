package com.pasyappagent.pasy.modul.home.transaction;

import com.pasyappagent.pasy.component.network.gson.GCashbackAgent;
import com.pasyappagent.pasy.component.network.gson.GTransaction;
import com.pasyappagent.pasy.component.network.gson.GTransactionTopup;

import java.util.List;

/**
 * Created by Dhimas on 12/21/17.
 */

public interface TransactionView {
    void onSuccessGetTransaction(List<GTransaction> response);

    void onSuccessGetTopup(List<GTransactionTopup> response);

    void onSuccessGetCashback(List<GCashbackAgent> response);

    void hideProgressList();
}
