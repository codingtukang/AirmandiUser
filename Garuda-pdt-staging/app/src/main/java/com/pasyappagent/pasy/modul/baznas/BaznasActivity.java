package com.pasyappagent.pasy.modul.baznas;

import android.content.Intent;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.modul.baznas.donate.DonateActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 11/3/17.
 */

public class BaznasActivity extends BaseActivity {
    private Button nextBtn;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.baznas_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Badan Amil Zakat Nasional");
    }

    @Override
    protected void onCreateAtChild() {
        nextBtn = (Button) findViewById(R.id.next_btn);
        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(BaznasActivity.this, DonateActivity.class));
            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }
}
