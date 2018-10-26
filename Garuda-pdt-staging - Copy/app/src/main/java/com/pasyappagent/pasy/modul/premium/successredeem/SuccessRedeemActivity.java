package com.pasyappagent.pasy.modul.premium.successredeem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.modul.home.HomePageActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 9/27/17.
 */

public class SuccessRedeemActivity extends AppCompatActivity {
    private Button okBtn;
    private TextView redeemItem;
    private TextView redeemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_redeem_activity);

        okBtn = (Button) findViewById(R.id.ok_btn);
        redeemId = (TextView) findViewById(R.id.order_id_redeem);
        redeemItem = (TextView) findViewById(R.id.redeem_name);
        String item = getIntent().getStringExtra("itemRedeem");
        String id = getIntent().getStringExtra("itemId");
        redeemId.setText("Reward id : " + id);
        redeemItem.setText(item);
        RxView.clicks(okBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(SuccessRedeemActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
