package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLog;

import java.util.List;

public class TransferRequestLogResponse {

    public boolean success;
    public String message;
    public List<GTransferRequestLog> logs;
    public GCostumer customer;
    public GCostumer to_customer;
}
