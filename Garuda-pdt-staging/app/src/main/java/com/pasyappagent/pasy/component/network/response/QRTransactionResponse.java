package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GTransaction;
import com.pasyappagent.pasy.component.network.gson.GTransactionTopup;

/**
 * Created by Dhimas on 2/1/18.
 */

public class QRTransactionResponse {
    public boolean success;
    public QRResponse item;
    public String message;
    public GTransaction transaction;
}
