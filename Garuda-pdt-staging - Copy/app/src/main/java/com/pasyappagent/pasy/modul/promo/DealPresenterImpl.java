package com.pasyappagent.pasy.modul.promo;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GDeals;
import com.pasyappagent.pasy.component.network.response.DealsResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Dhimas on 4/26/18.
 */

public class DealPresenterImpl implements DealPresenter {
    private CommonInterface cInterface;
    private SuccessResponseDeals listener;
    private DealsInteractor mInteractor;

    public DealPresenterImpl(CommonInterface cInterface, SuccessResponseDeals listener) {
        this.cInterface = cInterface;
        this.listener = listener;
        mInteractor = new DealsInteractorImpl(this.cInterface.getService());
    }

    @Override
    public void getDeals() {
        cInterface.showProgressLoading();
        mInteractor.getDeals().subscribe(new Subscriber<DealsResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(DealsResponse dealsResponse) {
                cInterface.hideProgresLoading();
                if (listener != null) {
                    listener.onSuccessDeals(dealsResponse.items);
                }
            }
        });
    }

    public interface SuccessResponseDeals {
        void onSuccessDeals(List<GDeals> deals);
    }
}
