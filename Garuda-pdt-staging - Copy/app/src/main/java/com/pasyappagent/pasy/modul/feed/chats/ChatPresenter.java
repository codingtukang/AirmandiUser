package com.pasyappagent.pasy.modul.feed.chats;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLog;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLogGroup;
import com.pasyappagent.pasy.component.network.response.SyncContactResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogGroupResponse;
import com.pasyappagent.pasy.component.network.response.TransferRequestLogResponse;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.customer.ContactCustomerInteractor;
import com.pasyappagent.pasy.modul.customer.ContactCustomerPresenter;

import java.util.List;

import rx.Subscriber;

public class ChatPresenter {
    private CommonInterface cInterface;
    private ChatInteractor mInteractor;
    private ChatView chatView;

    public ChatPresenter(CommonInterface cInterface, ChatView listener) {
        this.cInterface = cInterface;
        mInteractor = new ChatInteractor(this.cInterface.getService());
        chatView = listener;
    }

    public void getLogGroups() {
        cInterface.showProgressLoading();
        mInteractor.getTransferRequestLogGroups().subscribe(new Subscriber<TransferRequestLogGroupResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransferRequestLogGroupResponse response) {
                cInterface.hideProgresLoading();
                if (response.success){
                    if (chatView != null) {
                        chatView.successGetTransferRequestLogGroups(response.log_groups);
                    }
                }
                else{
                    cInterface.onFailureRequest(response.message);
                }

            }
        });
    }

    public void getLogs(String to_customer_id) {
        cInterface.showProgressLoading();
        mInteractor.getTransferRequestLogs(to_customer_id).subscribe(new Subscriber<TransferRequestLogResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransferRequestLogResponse response) {
                cInterface.hideProgresLoading();
                if (response.success){
                    if (chatView != null) {
                        chatView.successGetTransferRequestLogs(response.logs, response.customer, response.to_customer);
                    }
                }
                else{
                    cInterface.onFailureRequest(response.message);
                }

            }
        });
    }

    public interface ChatView {
        void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups);
        void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer);
    }
}
