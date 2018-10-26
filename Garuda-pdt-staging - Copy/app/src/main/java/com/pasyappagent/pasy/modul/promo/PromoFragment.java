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
import com.pasyappagent.pasy.component.adapter.RecyPromoAdapter;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GPromo;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.promo.detail.PromoDetailActivity;

import java.util.List;

/**
 * Created by Dhimas on 3/26/18.
 */

public class PromoFragment extends Fragment implements PromoPresenterImpl.EventPromoPresenter, CommonInterface, RecyPromoAdapter.ActionPromoAdapter {
    private RecyclerView promoList;
    private RecyPromoAdapter mAdapter;
    private PromoPresenter mPresenter;
    private CustomProgressBar progressBar = new CustomProgressBar();

    public PromoFragment newInstance() {
        PromoFragment fragment = new PromoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promo_fragment_layout, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new PromoPresenterImpl(this, this);
        mPresenter.getListPromo();
    }

    private void initComponent(View view) {
        mAdapter = new RecyPromoAdapter(this);
        promoList = (RecyclerView) view.findViewById(R.id.promo_list);
        promoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //promoList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        promoList.setAdapter(mAdapter);
    }

    @Override
    public void onSuccessGetList(List<GPromo> promoList) {
        mAdapter.setDataPromo(promoList);
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

    @Override
    public void onClickPromo(String promoId) {
        Intent intent = new Intent(getActivity(), PromoDetailActivity.class);
        intent.putExtra("promoId", promoId);
        startActivity(intent);
    }
}
