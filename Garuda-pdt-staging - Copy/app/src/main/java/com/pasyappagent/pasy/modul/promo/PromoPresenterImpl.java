package com.pasyappagent.pasy.modul.promo;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.network.response.PromoResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Dhimas on 2/18/18.
 */

public class PromoPresenterImpl implements PromoPresenter {
    private CommonInterface cInterface;
    private PromoInteractor mInteractor;
    private EventPromoPresenter listener;

    public PromoPresenterImpl(CommonInterface commonInterface, EventPromoPresenter listener) {
        cInterface = commonInterface;
        mInteractor = new PromoInteractorImpl(cInterface.getService());
        this.listener = listener;
    }

    @Override
    public void getListPromo() {
        cInterface.showProgressLoading();
        mInteractor.getList().subscribe(new Subscriber<PromoResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(PromoResponse promoResponse) {
                cInterface.hideProgresLoading();
                if (listener != null) {
                    listener.onSuccessGetList(promoResponse.items);
                }
            }
        });
    }

    public interface EventPromoPresenter{
        void onSuccessGetList(List<GPromo> promoList);
    }
}
