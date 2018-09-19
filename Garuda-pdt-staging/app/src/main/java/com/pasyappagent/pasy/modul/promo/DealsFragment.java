package com.pasyappagent.pasy.modul.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyDealsAdapter;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GDeals;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.util.List;

/**
 * Created by Dhimas on 3/27/18.
 */

public class DealsFragment extends Fragment implements RecyDealsAdapter.ActionDealsAdapter, DealPresenterImpl.SuccessResponseDeals, CommonInterface {
    private RecyclerView dealsList;
    private RecyDealsAdapter mAdapter;
    private DealPresenter mPresenter;
    private CustomProgressBar progressBar = new CustomProgressBar();

    public DealsFragment newInstance() {
        DealsFragment fragment = new DealsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promo_fragment_layout, container, false);
        initComponent(view);
        mPresenter = new DealPresenterImpl(this, this);
        mPresenter.getDeals();
        return view;
    }

    private void initComponent(View view) {
        mAdapter = new RecyDealsAdapter(this);
        dealsList = (RecyclerView) view.findViewById(R.id.promo_list);
        dealsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        dealsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        dealsList.setAdapter(mAdapter);
    }

    @Override
    public void onClickDeals(GDeals deal) {

    }

    @Override
    public void onSuccessDeals(List<GDeals> deals) {
        mAdapter.setDeals(deals);
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(getContext(), "", false, null);
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
        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
    }
}
