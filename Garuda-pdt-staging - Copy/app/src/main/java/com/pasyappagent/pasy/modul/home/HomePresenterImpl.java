package com.pasyappagent.pasy.modul.home;

import com.google.gson.JsonObject;
import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GBalance;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/27/17.
 */

public class HomePresenterImpl implements HomePresenter {
    private CommonInterface cInterface;
    private HomeView mView;
    private HomeInteractor mInteractore;

    public HomePresenterImpl(CommonInterface cInterface, HomeView view) {
        this.cInterface = cInterface;
        mView = view;
        mInteractore = new HomeInteractorImpl(cInterface.getService());
    }

    @Override
    public void getDataHomeActivity() {
        cInterface.showProgressLoading();
        mInteractore.getBalance().subscribe(new Subscriber<GBalance>() {
            @Override
            public void onCompleted() {
                cInterface.hideProgresLoading();
            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GBalance gBalance) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetBalance(gBalance.balance);
                checkPremium();
            }
        });
    }

    @Override
    public void checkPremium() {
        cInterface.showProgressLoading();
        mInteractore.checkPremiumAgent().subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {
                cInterface.hideProgresLoading();
            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                cInterface.hideProgresLoading();
                mView.onSuccessCheckPremium(jsonObject.get("premium").getAsBoolean());
            }
        });
    }
}
