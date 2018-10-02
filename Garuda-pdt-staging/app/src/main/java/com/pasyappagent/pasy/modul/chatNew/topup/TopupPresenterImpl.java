package com.pasyappagent.pasy.modul.chatNew.topup;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.response.VoucherResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/27/17.
 */

public class TopupPresenterImpl implements TopupPresenter {
    private CommonInterface cInterface;
    private TopupView mView;
    private TopupInteractor mInteractor;

    public TopupPresenterImpl(CommonInterface commonInterface, TopupView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new TopupInteractorImpl(cInterface.getService());
    }

    @Override
    public void checkVoucher(String voucher, String amount) {
        cInterface.showProgressLoading();
        mInteractor.checkVoucher(voucher, amount).subscribe(new Subscriber<VoucherResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(VoucherResponse voucherResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessRequestVoucher(voucherResponse.voucher);
            }
        });
    }

    @Override
    public void topup(String amount, String voucherId, String method) {
        cInterface.showProgressLoading();
        mInteractor.topup(amount, voucherId, method).subscribe(new Subscriber<GTopup>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GTopup gTopup) {
                cInterface.hideProgresLoading();
                mView.onSuccessRequest(gTopup);
            }
        });
    }
}
