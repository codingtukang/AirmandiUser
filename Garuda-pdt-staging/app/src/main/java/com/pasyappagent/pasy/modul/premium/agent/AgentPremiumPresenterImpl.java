package com.pasyappagent.pasy.modul.premium.agent;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 12/19/17.
 */

public class AgentPremiumPresenterImpl implements AgentPremiumPresenter {
    private CommonInterface cInterface;
    private AgentPremiumView mView;
    private AgentPremiumInteractor mInteractor;

    public AgentPremiumPresenterImpl(CommonInterface commonInterface, AgentPremiumView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new AgentPremiumInteractorImpl(cInterface.getService());
    }

    @Override
    public void getCashback() {
        cInterface.showProgressLoading();
        mInteractor.getCashback().subscribe(new Subscriber<GCashback>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GCashback cashback) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetCashback(cashback);
                getDownline();
            }
        });
    }

    @Override
    public void getDownline() {
        cInterface.showProgressLoading();
        mInteractor.getDownline().subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetDownline(jsonObject.get("total_downline").toString());
            }
        });
    }

    @Override
    public void redeemCashback() {
        cInterface.showProgressLoading();
        mInteractor.redeemCashback().subscribe(new Subscriber<GCashbackRedeem>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GCashbackRedeem jsonObject) {
                cInterface.hideProgresLoading();
                mView.onSuccessRedeem(jsonObject);
            }
        });
    }
}
