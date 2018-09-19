package com.pasyappagent.pasy.modul.premium.redeem;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RedeemPresenterImpl implements RedeemPresenter{
    private CommonInterface cInterface;
    private RedeemView mView;
    private RedeemInteractor mInteractor;

    public RedeemPresenterImpl(CommonInterface cInterface, RedeemView view) {
        mView = view;
        this.cInterface = cInterface;
        mInteractor = new RedeemInteractorImpl(this.cInterface.getService());
    }

    @Override
    public void getReward() {
        cInterface.showProgressLoading();
        mInteractor.getReward().subscribe(new Subscriber<List<GReward>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(List<GReward> gRewards) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetReward(gRewards);

            }
        });

    }

    @Override
    public void redeemReward(String rewardId) {
        cInterface.showProgressLoading();
        mInteractor.redeemReward(rewardId).subscribe(new Subscriber<GRedeem>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GRedeem redeem) {
                cInterface.hideProgresLoading();
                mView.onSuccessRedeemReward(redeem);
            }
        });
    }
}
