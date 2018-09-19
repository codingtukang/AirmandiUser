package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GLogo;
import com.pasyappagent.pasy.component.network.gson.GMerchant;
import com.pasyappagent.pasy.component.network.gson.GVoucher;

/**
 * Created by Dhimas on 2/1/18.
 */

public class QRResponse {
    public String id;
    public String merchant_id;
    public String amount;
    public String notes;
    public GLogo image;
    public String code;
    public GMerchant merchant;
    public String is_used;
    public GVoucher voucher;
    public String voucher_id;
}
