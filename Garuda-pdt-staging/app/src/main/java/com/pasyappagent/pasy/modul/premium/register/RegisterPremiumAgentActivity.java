package com.pasyappagent.pasy.modul.premium.register;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.response.TransactionTopupResponse;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.topup.topupstatus.StatusTopupActivity;
import com.pasyappagent.pasy.modul.topup.topuptransfer.TransferTopupActivity;
import com.pasyappagent.pasy.modul.transactionreview.TransactionReviewActivity;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/6/17.
 */

public class RegisterPremiumAgentActivity extends BaseActivity {
    private static int CODE_TRANSACTION = 1;
    public static String DATE_TIME = "dateTime";
    public static String ORDER_ID = "orderId";
    public static String IS_SUCCESS = "isSuccess";
    public static String MESSAGE = "message";
    private TextView registerInfoFirst;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.register_premium_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("AGEN PREMIUM");

        Button nextBtn = (Button) findViewById(R.id.accept_premium);
        registerInfoFirst = (TextView) findViewById(R.id.text_info_first);

//        if (PreferenceManager.getStatusAkupay()) {
//            registerInfoFirst.setText("Biaya premium adalah Rp 150.000 dan akan langsung dibayarkan melalui saldo Aku pay");
//        } else {
//            registerInfoFirst.setText("Biaya premium adalah Rp 150.000 dan akan langsung dibayarkan melalui saldo Pasy Agent");
//        }

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(RegisterPremiumAgentActivity.this, TransactionReviewActivity.class);
                intent.putExtra(TransactionReviewActivity.TOTAL_TRANSACTION, "150000");
                intent.putExtra(TransactionReviewActivity.ORDER_ID, "");
                intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO, "Pendaftaran Agen Premium");
                intent.putExtra(TransactionReviewActivity.REQUEST_FROM, 100);
                startActivityForResult(intent, CODE_TRANSACTION);
            }
        });
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String orderId = data.getExtras().getString(ORDER_ID);
            String message = data.getExtras().getString(MESSAGE);
            boolean isSuccess = data.getExtras().getBoolean(IS_SUCCESS);
            String dateTime = data.getExtras().getString(DATE_TIME);
            Intent intent = new Intent(this, StatusTopupActivity.class);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("id")).parse(dateTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm : ss");
                TransactionTopupResponse topupResponse = new TransactionTopupResponse();
                topupResponse.setDate(dateFormat.format(date));
                topupResponse.setTime(timeFormat.format(date));
                topupResponse.setSuccess(isSuccess);
                topupResponse.setOrderId(orderId);
                topupResponse.setInfo(message);
                topupResponse.setTopupSaldo("150000");
                intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(topupResponse));
                startActivity(intent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
