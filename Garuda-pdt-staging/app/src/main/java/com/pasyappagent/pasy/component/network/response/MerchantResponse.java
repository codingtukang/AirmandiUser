package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GMerchant;
import com.pasyappagent.pasy.component.network.gson.GPagination;

import java.util.List;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantResponse {
    public List<GMerchant> items;

    public GPagination pagination;
}
