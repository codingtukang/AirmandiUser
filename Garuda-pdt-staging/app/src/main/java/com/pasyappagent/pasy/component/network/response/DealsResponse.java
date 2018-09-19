package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GDeals;
import com.pasyappagent.pasy.component.network.gson.GPagination;

import java.util.List;

/**
 * Created by Dhimas on 4/26/18.
 */

public class DealsResponse {
    public List<GDeals> items;
    public GPagination pagination;
}
