package com.pasyappagent.pasy.modul.customer;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.SyncContactResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactCustomerInteractor {
    private NetworkService mService;

    public ContactCustomerInteractor(NetworkService mService) {
        this.mService = mService;
    }

    public Observable<SyncContactResponse> syncContacts(String mobiles) {
        return mService.syncContact(mobiles).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
