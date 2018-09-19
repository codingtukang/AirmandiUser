package com.pasyappagent.pasy.modul.login.passcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.login.LoginActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenter;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenterImpl;
import com.pasyappagent.pasy.modul.register.otp.OtpView;

import rx.functions.Action1;

/**
 * Created by Dhimas on 11/24/17.
 */

public class PasscodeLoginActivity extends AppCompatActivity implements CommonInterface, PasscodeLoginView{
    private static final String TITLE = "title_passcode";
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private TextView greetingTxt;
    private IndicatorDots mIndicatorDots;
    private String mobile;
    private PasscodeLoginPresenter mPresenter;
    private RelativeLayout bottomMenu;
    private TextView changeAccount;
    private TextView forgotPin;
    private TextView phoneNUmber;
    private ImageView passImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_layout);
        mPresenter = new PasscodeLoginPresenterImpl(this, this);
        mobile = getIntent().getStringExtra(OtpActivity.MOBILE);
        initComponent();
    }

    private void initComponent() {
        greetingTxt = (TextView) findViewById(R.id.greeting_text);
        bottomMenu = (RelativeLayout) findViewById(R.id.bottom_menu);
        PinLockView mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        changeAccount = (TextView) findViewById(R.id.change_account);
        forgotPin = (TextView) findViewById(R.id.forgot_pin);
        phoneNUmber = (TextView) findViewById(R.id.phone_number);
        passImg = (ImageView) findViewById(R.id.image_passcode);

//        if (!PreferenceManager.getStatusAkupay()) {
//            passImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }
        String title = getIntent().getStringExtra(TITLE);
        greetingTxt.setText(title);
        bottomMenu.setVisibility(View.VISIBLE);

        if (PreferenceManager.isLogin()) {
            phoneNUmber.setText(mobile);
            String[] agent = PreferenceManager.getUser();
            greetingTxt.setText(agent[0]);
        } else {
            greetingTxt.setText("SELAMAT DATANG");
        }


        RxView.clicks(changeAccount).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoLoginPage();
            }
        });

        RxView.clicks(forgotPin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(PasscodeLoginActivity.this, ForgotPassActivity.class));
            }
        });
    }

    private void gotoLoginPage() {
        PreferenceManager.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            mPresenter.login(mobile, pin, PreferenceManager.getImei());
        }

        @Override
        public void onEmpty() {
        }


        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

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
    }

    @Override
    public void onSuccessRequest() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressBar != null && progressBar.getDialog() != null) {
            progressBar.getDialog().dismiss();
        }
    }
}
