package com.pasyappagent.pasy.modul.sendbalance;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/29/17.
 */

public class SendBalancePresenterImpl implements SendBalancePresenter {
    private CommonInterface cInterface;
    private SendBalanceView mView;
    private SendBalanceInteractor mInteractor;

    public SendBalancePresenterImpl(CommonInterface commonInterface, SendBalanceView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new SendBalanceInteractorImpl(cInterface.getService());
    }

    @Override
    public void sendBalance(String mobile, String amount, String notes) {
        cInterface.showProgressLoading();
        mInteractor.transferBalance(mobile, amount, notes).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessSendBalance();
            }
        });
    }
}
