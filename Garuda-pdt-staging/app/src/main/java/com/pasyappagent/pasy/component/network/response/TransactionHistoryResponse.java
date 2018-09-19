package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GPagination;
import com.pasyappagent.pasy.component.network.gson.GTransaction;

import java.util.List;

/**
 * Created by Dhimas on 12/21/17.
 */

public class TransactionHistoryResponse {
    public List<GTransaction> items;
    public GPagination pagination;
}
