package com.pasyappagent.pasy.modul.customer;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.network.response.SyncContactResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

public class ContactCustomerPresenter {
    private CommonInterface cInterface;
    private ContactCustomerInteractor mInteractor;
    private SuccessSync sSync;

    public ContactCustomerPresenter(CommonInterface cInterface, SuccessSync listener) {
        this.cInterface = cInterface;
        mInteractor = new ContactCustomerInteractor(this.cInterface.getService());
        sSync = listener;
    }

    public void syncContacts(String mobiles) {
        cInterface.showProgressLoading();
        mInteractor.syncContacts(mobiles).subscribe(new Subscriber<SyncContactResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SyncContactResponse response) {
                cInterface.hideProgresLoading();
                if (sSync != null) {
                    sSync.setData(response.contacts);
                }
            }
        });
    }

    public interface SuccessSync {
        void setData(List<GCostumer> customers);
    }
}
