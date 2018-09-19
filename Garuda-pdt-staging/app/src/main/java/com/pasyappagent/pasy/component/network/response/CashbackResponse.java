package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GCashbackAgent;
import com.pasyappagent.pasy.component.network.gson.GPagination;

import java.util.List;

/**
 * Created by Dhimas on 12/26/17.
 */

public class CashbackResponse {
    public List<GCashbackAgent> items;
    public GPagination pagination;
}
