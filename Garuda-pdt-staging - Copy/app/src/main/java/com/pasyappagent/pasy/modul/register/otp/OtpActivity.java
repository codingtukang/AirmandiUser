package com.pasyappagent.pasy.modul.register.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeActivity;

import rx.functions.Action1;


/**
 * Created by Dhimas on 10/5/17.
 */

public class OtpActivity extends AppCompatActivity implements CommonInterface, OtpView {
    public static final String MOBILE = "mobile_text";
    public static final String IS_FORGOT = "forgot";
    private TextView countdownText;
    private TextView resendText;
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private OtpPresenter mPresenter;
    private boolean isFinishCountdown = false;
    private boolean isForgot;
    private ImageView otpImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        mPresenter = new OtpPresenterImpl(this, this);
        final String mobile = getIntent().getStringExtra(MOBILE);
        isForgot = getIntent().getBooleanExtra(IS_FORGOT, false);
        PinEntryEditText entryEditText = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (entryEditText != null) {
            entryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length() == 6) {
                        mPresenter.verifyAgent(str.toString(), mobile);
                    }
                }
            });
        }
        countdownText = (TextView) findViewById(R.id.countdown_text);
        resendText = (TextView) findViewById(R.id.resend_text);
        otpImg = (ImageView) findViewById(R.id.image_otp);

//        if (!PreferenceManager.getStatusAkupay()) {
//            otpImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }

        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownText.setText("Kirim ulang kode dalam : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isFinishCountdown = true;
                countdownText.setText("");
            }
        }.start();

        RxView.clicks(resendText).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isFinishCountdown) {
                    countDownTimer.start();
                    isFinishCountdown = false;
                    mPresenter.resendCode(mobile);
                }
            }
        });

    }



    @Override
    public void showProgressLoading() {
        progressBar.show(this, "Loading", false, null);
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
    }

    @Override
    public void onSuccessRequest() {
        Intent intent = new Intent(this, PasscodeActivity.class);
        intent.putExtra(IS_FORGOT, isForgot);
        startActivity(intent);
    }

    @Override
    public void onSuccessResendCode(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
