package com.pasyappagent.pasy.modul.home.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.ListActionLoadMore;
import com.pasyappagent.pasy.component.adapter.RecyTransactionAdapter;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GCashbackAgent;
import com.pasyappagent.pasy.component.network.gson.GTransaction;
import com.pasyappagent.pasy.component.network.gson.GTransactionTopup;
import com.pasyappagent.pasy.component.network.response.CashbackResponse;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionHistoryResponse;
import com.pasyappagent.pasy.component.network.response.TransactionTopupResponse;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.topup.topupstatus.StatusTopupActivity;
import com.pasyappagent.pasy.modul.topup.topuptransfer.TransferTopupActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Dhimas on 9/25/17.
 */

public class TransactionFragment extends Fragment implements CommonInterface, TransactionView, RecyTransactionAdapter.OnListClicked, ListActionLoadMore {
    private RecyclerView listView;
    private RecyTransactionAdapter mAdapter;
    private CustomProgressBar progressBar = new CustomProgressBar();
    private TransactionPresenter mPresenter;
    private RelativeLayout empty;
    private ImageView emptyImg;
    private TextView emptyText;
    private static String IS_PPOB = "isPPob";
    private boolean isPPOB;

    public TransactionFragment newInstance(boolean isPPOB) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_PPOB, isPPOB);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment_layout, container, false);
        initComponent(view);
        isPPOB = getArguments().getBoolean(IS_PPOB);
        mPresenter = new TransactionPresenterImpl(this, this);
        if (isPPOB) {
            mPresenter.getTransactionPPOB();
        } else {
            mPresenter.getTransaction();
        }

        return view;
    }

    private void initComponent(View view) {
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        emptyImg = (ImageView) view.findViewById(R.id.img_empty);
        emptyText = (TextView) view.findViewById(R.id.text_empty);
        listView = (RecyclerView) view.findViewById(R.id.transaction_list);
        mAdapter = new RecyTransactionAdapter(this, isPPOB);
        mAdapter.setListenerOnClick(this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(mAdapter);
    }

    @Override
    public void showProgressLoading() {
//        progressBar.show(getContext(), "", false, null);
    }

    @Override
    public void hideProgresLoading() {
//        progressBar.getDialog().dismiss();
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
    public void onSuccessGetTransaction(List<GTransaction> response) {
        mAdapter.addAll(response);
        if (response.size() < 1) {
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            emptyText.setText("Saat ini belum ada transaksi pembayaran layanan");
            emptyImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_service));
        }
    }

    @Override
    public void onSuccessGetTopup(List<GTransactionTopup> response) {

    }

    @Override
    public void onSuccessGetCashback(List<GCashbackAgent> response) {

    }

    @Override
    public void hideProgressList() {
        mAdapter.removeLoadingList();
    }

    @Override
    public void listClick(GTransaction transaction) {
        if (isPPOB) {
            TransactionTopupResponse response = new TransactionTopupResponse();
            response.setFail(false);
            String service = transaction.service != null ? transaction.service.name : transaction.notes;
            String note = service + ", " + transaction.customer_no;
//            int service = Integer.parseInt(transaction.service.category);
            switch (transaction.service.category) {
                case Constant.SERVICE_PLN:
                    if (!TextUtils.isEmpty(transaction.data)) {
                        try {
                            JSONObject json = new JSONObject(transaction.data);
                            if (json.has("harga")) {
                                String token = json.get("token").toString();
                                note = "Token PLN " + Integer.parseInt(json.get("harga").toString()) + " ke " +
                                        json.get("idmeter") + " SUKSES. SN: " +
                                        json.get("token").toString() + "/" +
                                        json.get("nama").toString() +
                                        "/" + json.get("kwh").toString().replaceFirst("^0+(?!$)", "");
                            } else {
                                String token = json.get("token").toString();
                                note = "Token PLN " + json.get("price").toString() + " ke " +
                                        json.get("meter_serial") + " SUKSES. SN: " +
                                        MethodUtil.formatTokenNumber(token) + "/" +
                                        json.get("customer_name").toString() +
                                        "/" + json.get("total_kwh").toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject json = new JSONObject(transaction.data);
                            note = "Pembayaran PLN dengan no " + json.get("customer_id").toString() + " atas nama " + json.get("customer_name").toString()
                                    + " untuk bulan " + json.get("bill_period").toString() + ", kWh : " + json.get("fare_power").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constant.SERVICE_BPJS:
                    try {
                        JSONObject json = new JSONObject(transaction.data);
                        note = "Pembayaran BPJS dengan no " + json.get("customer_id").toString() +
                                " atas nama " + json.get("customer_name").toString() + " Sukses.";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.SERVICE_PDAM:
                    try {
                        JSONObject json = new JSONObject(transaction.data);
                        if (!TextUtils.isEmpty(json.get("provider_code").toString())) {
                            note = "Pembayaran PAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        } else if (!TextUtils.isEmpty(json.get("idpel").toString())) {
                            note = "Pembayaran PDAM dengan no " + json.get("idpel").toString() +
                                    " atas nama " + json.get("namapelanggan").toString() + " Sukses.";
                        } else {
                            note = "Pembayaran PDAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            response.setDate(dateTime[0]);
            response.setTime(dateTime[1]);
            response.setSuccess(true);
            response.setOrderId(transaction.id);
            response.setInfo(note);
            response.setTopupSaldo(transaction.default_price);
            response.setFromHome(true);
            Intent intent = new Intent(getActivity(), StatusTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
            startActivity(intent);
        } else {
            TransactionTopupResponse response = new TransactionTopupResponse();
            response.setFail(false);
            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            response.setDate(dateTime[0]);
            response.setTime(dateTime[1]);
            if (!TextUtils.isEmpty(transaction.status_label)) {
                switch (transaction.status_label) {
                    case "waiting payment":
                        response.setSuccess(false);
                        break;
                    case "success":
                        response.setSuccess(true);
                        break;
                    default:
                        response.setFail(true);
                        break;
                }
            } else {
                if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS)) {
                    response.setSuccess(true);
                } else {
                    response.setSuccess(false);
                }
            }
            response.setOrderId(transaction.id);
            response.setInfo(transaction.notes);
            response.setTopupSaldo(transaction.amount_charged);
            response.setFromHome(true);
            Intent intent = new Intent(getActivity(), StatusTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
            startActivity(intent);
        }
    }

    @Override
    public void onLoadMoreList() {
        if (isPPOB) {
            mPresenter.loadNextTransactionPPOB();
        } else {
            mPresenter.loadNextTransaction();
        }

    }
}
