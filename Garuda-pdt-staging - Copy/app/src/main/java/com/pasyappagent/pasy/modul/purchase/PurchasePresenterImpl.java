package com.pasyappagent.pasy.modul.purchase;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.response.ServicesResponse;
import com.pasyappagent.pasy.component.network.response.TransactionResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 12/14/17.
 */

public class PurchasePresenterImpl implements PurchasePresenter{
    private PurchaseView mView;
    private CommonInterface cInterface;
    private PurchaseInteractor mInteractor;

    public PurchasePresenterImpl(CommonInterface cInterface, PurchaseView view) {
        mView = view;
        this.cInterface = cInterface;
        mInteractor = new PurchaseInteractorImpl(this.cInterface.getService());
    }


    @Override
    public void getServices(String type, String amount, String no, String cat) {
        cInterface.showProgressLoading();
        mInteractor.getServices(type, amount, no, cat).subscribe(new Subscriber<ServicesResponse>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(ServicesResponse servicesResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetService(servicesResponse.services);
            }
        });
    }

    @Override
    public void setInquiry(String serviceId, String customerNo, boolean isUsing, String amount) {
        cInterface.showProgressLoading();
        if (isUsing) {
            mInteractor.setInquiry(serviceId, customerNo, amount).subscribe(new Subscriber<TransactionResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    cInterface.hideProgresLoading();
                    cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                }

                @Override
                public void onNext(TransactionResponse transactionResponse) {
                    cInterface.hideProgresLoading();
                    if (transactionResponse.success) {
                        mView.onSuccessInquiry(transactionResponse.transactions);
                    } else {
                        cInterface.onFailureRequest(transactionResponse.message);
                    }

                }
            });
        } else {
            mInteractor.getTransaction(serviceId, customerNo).subscribe(new Subscriber<TransactionResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    cInterface.hideProgresLoading();
                    cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                }

                @Override
                public void onNext(TransactionResponse transactionResponse) {
                    cInterface.hideProgresLoading();
                    mView.onSuccessInquiry(transactionResponse.transactions);
                }
            });
        }
    }
}
