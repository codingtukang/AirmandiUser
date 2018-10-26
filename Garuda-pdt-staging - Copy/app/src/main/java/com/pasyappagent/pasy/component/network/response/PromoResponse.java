package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GPagination;
import com.pasyappagent.pasy.component.network.gson.GPromo;

import java.util.List;

/**
 * Created by Dhimas on 2/16/18.
 */

public class PromoResponse {
    public List<GPromo> items;
    public GPagination pagination;
}
