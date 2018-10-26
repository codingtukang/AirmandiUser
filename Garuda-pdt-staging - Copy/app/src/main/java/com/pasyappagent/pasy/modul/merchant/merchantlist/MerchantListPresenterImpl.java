package com.pasyappagent.pasy.modul.merchant.merchantlist;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GMerchant;
import com.pasyappagent.pasy.component.network.response.MerchantResponse;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantListPresenterImpl implements MerchantListPresenter {
    private CommonInterface cInterface;
    private MerchantlistView mView;
    private int page = 0;
    private List<GMerchant> merchant;
    private String mQuery;

    public MerchantListPresenterImpl(CommonInterface cInterface, MerchantlistView view) {
        this.page = 1;
        mView = view;
        this.cInterface = cInterface;
        merchant = new ArrayList<>();
    }

    @Override
    public void getMerchantList(String query) {
        mQuery = query;
        page = 1;
        getMerchantList(page, query).subscribe(new Subscriber<MerchantResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MerchantResponse merchantResponse) {
                merchant.addAll(merchantResponse.items);
                if (merchantResponse.pagination.has_next) {
                    merchantResponse.items.add(null);
                    page++;
                }
                if (merchantResponse.items != null) mView.onSuccess(merchantResponse.items);
            }
        });
    }

    @Override
    public void loadNextTransactionPage() {
        getMerchantList(page, mQuery).subscribe(new Subscriber<MerchantResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MerchantResponse merchantResponse) {
                merchant.addAll(merchantResponse.items);
                if (merchantResponse.pagination.has_next) {
                    merchantResponse.items.add(null);
                    page++;
                }
                mView.hideProgressList();
                if (merchantResponse.items != null) mView.loadMore(merchantResponse.items);
            }
        });
    }

    @Override
    public List<GMerchant> getMerchant() {
        return null;
    }

    Observable<MerchantResponse> getMerchantList(int page, String query) {
        return cInterface.getService().getMerchantList(page, query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
