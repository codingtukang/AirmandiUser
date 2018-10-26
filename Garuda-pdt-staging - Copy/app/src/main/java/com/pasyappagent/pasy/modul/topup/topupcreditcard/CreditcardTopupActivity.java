package com.pasyappagent.pasy.modul.topup.topupcreditcard;

import android.widget.EditText;
import android.widget.TextView;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;

/**
 * Created by Dhimas on 10/2/17.
 */

public class CreditcardTopupActivity extends BaseActivity {
    private TextView amount;
    private EditText cardNumber;
    private EditText date;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.creditcard_payment_topup;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Kartu Kredit");
        amount = (TextView) findViewById(R.id.amount);
        GTopup topup = PreferenceManager.getTopup();
        amount.setText("Rp. "+MethodUtil.toCurrencyFormat(topup.amount));
        cardNumber = (EditText) findViewById(R.id.card_number);
        date = (EditText) findViewById(R.id.date);
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
