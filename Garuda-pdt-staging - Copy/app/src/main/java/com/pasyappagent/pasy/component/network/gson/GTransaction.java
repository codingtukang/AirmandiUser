package com.pasyappagent.pasy.component.network.gson;

import java.util.List;

/**
 * Created by Dhimas on 12/18/17.
 */

public class GTransaction {
    public String id;
    public String agent_id;
    public String service_id;
    public String vendor_id;
    public String customer_no;
    public String amount;
    public String vendor_price;
    public String default_price;
    public String omzet;
    public String cashback_agent;
    public String status;
    public String account_id;
    public String image_id;
    public String notes;
    public String data;
    public String merchant_name;
    public String created_at;
    public String status_label;
    public String amount_charged;
    public String amount_transaction;
    public List<GLogo> images;
    public String customer_username;
    public GServices service;
    public GAgent agent;
    public GVendor vendor;
}
