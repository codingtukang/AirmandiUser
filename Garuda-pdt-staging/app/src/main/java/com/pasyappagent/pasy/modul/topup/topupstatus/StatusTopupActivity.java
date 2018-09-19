package com.pasyappagent.pasy.modul.topup.topupstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.response.TransactionTopupResponse;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.home.HomePageActivity;

import org.parceler.Parcels;

import rx.functions.Action1;

import static com.pasyappagent.pasy.modul.topup.topuptransfer.TransferTopupActivity.TOPUP_RESPONSE;

/**
 * Created by Dhimas on 9/29/17.
 */

public class StatusTopupActivity extends AppCompatActivity {
    private Button okBtn;
    private TextView greetingStatus;
    private TextView statusInfo;
    private TextView date;
    private TextView time;
    private TextView orderId;
    private TextView transactionInfo;
    private TextView bankName;
    private TextView notePayment;
    private ImageView bankImg;
    private TextView totalAmount;
    private ImageView statusLogo;
    private LinearLayout paymentMethod;
    private LinearLayout noteContainer;
    private TransactionTopupResponse topupResponse;
    private boolean isFromHome;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_topup_activity);
        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
        initComponent();
        initEvent();
    }

    private void initComponent() {
        okBtn = (Button) findViewById(R.id.ok_btn);
        greetingStatus = (TextView) findViewById(R.id.greeting_status);
        statusInfo = (TextView) findViewById(R.id.status_info);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        orderId = (TextView) findViewById(R.id.order_id_text);
        transactionInfo = (TextView) findViewById(R.id.transaction_info);
        bankName = (TextView) findViewById(R.id.bank_name);
        bankImg = (ImageView) findViewById(R.id.bank_img);
        statusLogo = (ImageView) findViewById(R.id.status_logo);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        paymentMethod = (LinearLayout) findViewById(R.id.payment_method);
        noteContainer = (LinearLayout) findViewById(R.id.payment_note_container);
        notePayment = (TextView) findViewById(R.id.notes_transfer);
    }

    private void initEvent() {
        RxView.clicks(okBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isFromHome) {
                    finish();
                } else {
                    Intent intent = new Intent(StatusTopupActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });

        if ((topupResponse.getStatus() != null && topupResponse.getStatus().equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) ||
                topupResponse.isFail()) {
            statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.reject));
            statusInfo.setText("Transaksi pembayaran tidak ditemukan");
            greetingStatus.setText("DITOLAK");
        } else {
            if (topupResponse.isSuccess()) {
                greetingStatus.setText("TERIMA KASIH");
                statusInfo.setText("Transaksi anda telah berhasil");
            } else {
                statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.process));
                statusInfo.setText("Sistem kami sedang melakukan verifikasi\n" +
                        "mungkin membutuhkan waktu");
                greetingStatus.setText("DALAM PROSES");
            }
        }

        if (!topupResponse.isTopupTransaction()) {
            paymentMethod.setVisibility(View.GONE);
        }

        if (topupResponse.isFromHome()) {
            isFromHome = true;
        }
        transactionInfo.setText(topupResponse.getInfo());
        time.setText(topupResponse.getTime());
        date.setText(topupResponse.getDate());
        orderId.setText(topupResponse.getOrderId());
        bankName.setText(topupResponse.getBankName());
        totalAmount.setText(MethodUtil.toCurrencyFormat(topupResponse.getTopupSaldo()));
        if (!TextUtils.isEmpty(topupResponse.getNotes())) {
            noteContainer.setVisibility(View.VISIBLE);
            notePayment.setText(topupResponse.getNotes());
        }
    }
}
