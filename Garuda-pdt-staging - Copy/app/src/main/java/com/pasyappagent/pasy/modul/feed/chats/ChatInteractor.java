package com.pasyappagent.pasy.modul.feed.chats;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.SyncContactResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogGroupResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatInteractor {
    private NetworkService mService;

    public ChatInteractor(NetworkService mService) {
        this.mService = mService;
    }

    public Observable<TransferRequestLogGroupResponse> getTransferRequestLogGroups() {
        return mService.getTransferRequestLogGroup().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<TransferRequestLogResponse> getTransferRequestLogs(String to_customer_id) {
        return mService.getTransferRequestLog(to_customer_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
