package com.pasyappagent.pasy.modul.checkpasscode;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.login.passcode.PasscodeLoginPresenter;
import com.pasyappagent.pasy.modul.login.passcode.PasscodeLoginPresenterImpl;
import com.pasyappagent.pasy.modul.login.passcode.PasscodeLoginView;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;

/**
 * Created by Dhimas on 11/29/17.
 */

public class CheckPasscodeActivity extends AppCompatActivity implements CommonInterface, PasscodeLoginView {
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private PasscodeLoginPresenter mPresenter;
    private String mobile;
    private ImageView passcodeImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_layout);
        TextView greetingTxt = (TextView) findViewById(R.id.greeting_text);
        greetingTxt.setText("");
        mPresenter = new PasscodeLoginPresenterImpl(this, this);
        mobile = getIntent().getStringExtra(OtpActivity.MOBILE);
        PinLockView mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        passcodeImg = (ImageView) findViewById(R.id.image_passcode);
//        if (!PreferenceManager.getStatusAkupay()) {
//            passcodeImg.setImageDrawable(getResources().getDrawable(R.drawable.pasy_agent));
//        }
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
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onSuccessRequest() {
        setResult(RESULT_OK);
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
}
