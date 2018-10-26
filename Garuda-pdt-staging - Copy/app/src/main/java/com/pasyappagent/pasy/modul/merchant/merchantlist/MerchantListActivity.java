package com.pasyappagent.pasy.modul.merchant.merchantlist;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.ListActionLoadMore;
import com.pasyappagent.pasy.component.adapter.RecyMerchantPayShopAdapter;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GMerchant;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.TextWatcherAdapter;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.merchant.detail.MerchantDetailActivity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantListActivity extends BaseActivity implements CommonInterface, MerchantlistView, ListActionLoadMore{
    private RecyMerchantPayShopAdapter mAdapter;
    private RecyclerView list;
    private MerchantListPresenter mPresenter;
    private EditText inputSearch;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.merchant_list_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Merchant");
        list = (RecyclerView) findViewById(R.id.merchants);
        inputSearch = (EditText) findViewById(R.id.search_input);
        mAdapter = new RecyMerchantPayShopAdapter(this);
        mAdapter.setOnListActionClicked(new RecyMerchantPayShopAdapter.Action() {
            @Override
            public void openMerchant(String merchantCode, String merchantId, String merchantName) {
                Intent intent = new Intent(MerchantListActivity.this, MerchantDetailActivity.class);
                intent.putExtra("merchantCode", merchantCode);
                startActivity(intent);
            }
        });
        list.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        list.setAdapter(mAdapter);
        //list.setFocusable(false);

//        RxTextView.textChanges(inputSearch).skip(2).subscribe(new Action1<CharSequence>() {
//            @Override
//            public void call(CharSequence charSequence) {
//                mPresenter.getMerchantList(inputSearch.getText().toString());
//            }
//        });

        inputSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.getMerchantList(inputSearch.getText().toString());
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new MerchantListPresenterImpl(this, this);
        mPresenter.getMerchantList("");
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();

    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    public void onSuccess(List<GMerchant> merchants) {
        mAdapter.clear();
        mAdapter.addAll(merchants);
    }

    @Override
    public void loadMore(List<GMerchant> merchants) {
        mAdapter.addAll(merchants);
    }

    @Override
    public void hideProgressList() {
        mAdapter.removeLoadingList();
    }

    @Override
    public void onLoadMoreList() {
        if (mPresenter != null) mPresenter.loadNextTransactionPage();
    }
}
