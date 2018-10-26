package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLog;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLogGroup;

import java.util.List;

public class TransferRequestLogGroupResponse {

    public boolean success;
    public String message;
    public List<GTransferRequestLogGroup> log_groups;
}
