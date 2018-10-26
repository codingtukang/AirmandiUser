package com.pasyappagent.pasy.modul.topup.topuptransfer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.PasyAgentApplication;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionTopupResponse;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.chat.ChatActivity;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.topup.topupmethod.PaymentMethodTopupPresenter;
import com.pasyappagent.pasy.modul.topup.topupmethod.PaymentMethodTopupPresenterImpl;
import com.pasyappagent.pasy.modul.topup.topupmethod.PaymentMethodTopupView;
import com.pasyappagent.pasy.modul.topup.topupstatus.StatusTopupActivity;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import rx.functions.Action1;

/**
 * Created by Dhimas on 9/29/17.
 */

public class TransferTopupActivity extends BaseActivity implements PaymentMethodTopupView, CommonInterface {
    public static final String TOPUP_RESPONSE = "topupResponse";
    public static final String IS_HOME = "isHome";
    private TransactionTopupResponse topupResponse;
    private Button nextBtn;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private LinearLayout containerTimer;
    private LinearLayout containerExpiredTime;
    private PaymentMethodTopupPresenter mPresenter;
    private CountDownTimer countDownTimer;
    private boolean isFromHome;
    private ImageView imgTransfer;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.transfer_payment_topup;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Bank Transfer");
        nextBtn = (Button) findViewById(R.id.next_btn);
        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
        isFromHome = getIntent().getBooleanExtra(IS_HOME, false);
        TextView totalAmount = (TextView) findViewById(R.id.total_amount);
        TextView orderIdText = (TextView) findViewById(R.id.order_id_text);
        TextView accountName = (TextView) findViewById(R.id.account_name);
        TextView accountNumber = (TextView) findViewById(R.id.account_number);
        TextView time = (TextView) findViewById(R.id.time);
        TextView date = (TextView) findViewById(R.id.date);
        hour = (TextView) findViewById(R.id.jam);
        minute = (TextView) findViewById(R.id.menit);
        second = (TextView) findViewById(R.id.detik);
        containerTimer = (LinearLayout) findViewById(R.id.container_countdown);
        containerExpiredTime = (LinearLayout) findViewById(R.id.note_expired_time);
        imgTransfer = (ImageView) findViewById(R.id.img_transfer);
        ImageView bankLogo = (ImageView) findViewById(R.id.bank_logo);

//        if (!PreferenceManager.getStatusAkupay()) {
            imgTransfer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pampasy_icon));
//        }
        totalAmount.setText("Rp "+ MethodUtil.toCurrencyFormat(topupResponse.getTopupSaldo()));
        orderIdText.setText(topupResponse.getOrderId());
        accountName.setText(topupResponse.getBankName());
        accountNumber.setText(topupResponse.getBankAccount());
        time.setText(topupResponse.getTime());
        date.setText(topupResponse.getDate());

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                Intent intent = new Intent(TransferTopupActivity.this, StatusTopupActivity.class);
//                topupResponse.setTopupTransaction(true);
//                intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
//                startActivity(intent);
                mPresenter.confirmPayment(topupResponse.getOrderId(), topupResponse.getBankId(), topupResponse.getAccountId());
            }
        });

        RxView.clicks(containerExpiredTime).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PasyAgentApplication app = (PasyAgentApplication) getApplication();
                Socket mSocket = app.getSocket();
                GAgent agent = PreferenceManager.getAgent();
                mSocket.emit("join room", agent.id);
                Intent intent = new Intent(TransferTopupActivity.this, ChatActivity.class);
                String[] user = PreferenceManager.getUser();
                intent.putExtra("username", user[0]);
                intent.putExtra("numUsers", 2);
                startActivity(intent);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        Date endDate = null;
        final long endTime = System.currentTimeMillis();
        try {
            endDate = formatter.parse(topupResponse.getExpiredAt());
            endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final NumberFormat f = new DecimalFormat("00");
        if (endDate != null) {
            countDownTimer = new CountDownTimer(endDate.getTime(), 1000) {

                public void onTick(long millisUntilFinished) {
                    long diff = millisUntilFinished - endTime;
                    long seconds = (diff / 1000) % 60 ;
                    if (seconds < 0) {
                        finishCountdown();
                    }
                    second.setText(f.format(seconds));
                    long minutes = ((diff / (1000*60)) % 60);
                    minute.setText(f.format(minutes));
                    long hours   = ((diff / (1000*60*60)) % 24);
                    hour.setText(f.format(hours));
                }

                public void onFinish() {
                    containerTimer.setVisibility(View.GONE);
                    containerExpiredTime.setVisibility(View.VISIBLE);
                }
            }.start();
        }

    }

    private void  finishCountdown() {
        countDownTimer.cancel();
        countDownTimer.onFinish();
        nextBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.charcoal));
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PaymentMethodTopupPresenterImpl(this, this);
    }



    @Override
    protected void onBackBtnPressed() {
        if (isFromHome) {
            onBackPressed();
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
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
    public void onSuccessRequest(TopupResponse response) {
        if (response.status == Integer.parseInt(Constant.TOPUP_STATUS_SUCCESS)) {
            topupResponse.setSuccess(true);
        }
        Intent intent = new Intent(TransferTopupActivity.this, StatusTopupActivity.class);
        topupResponse.setTopupTransaction(true);
        topupResponse.setBankName(response.account.bank.name);
        intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
        startActivity(intent);
    }

    @Override
    public void onSuccessRequestBanks(List<GBanks> listBank) {

    }
}
