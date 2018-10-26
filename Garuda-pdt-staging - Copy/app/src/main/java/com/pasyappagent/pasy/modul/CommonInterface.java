package com.pasyappagent.pasy.modul;

import com.pasyappagent.pasy.component.network.NetworkService;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface CommonInterface {
    void showProgressLoading();

    void hideProgresLoading();

    NetworkService getService();

    void onFailureRequest(String msg);
}
