package com.pasyappagent.pasy.modul.home.transaction;

import android.support.v4.app.FragmentTransaction;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;

/**
 * Created by Dhimas on 2/1/18.
 */

public class AllTransactionActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.all_transaction_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Riwayat Transaksi");
    }

    @Override
    protected void onCreateAtChild() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_home, new AllTransactionFragment().newInstance());
        ft.commit();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
