package com.pasyappagent.pasy.modul.login.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.login.LoginActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenter;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenterImpl;
import com.pasyappagent.pasy.modul.register.otp.OtpView;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/30/17.
 */

public class ForgotPassActivity extends AppCompatActivity implements CommonInterface, OtpView{
    private TextView registerText;
    private EditText mobile;
    private Button loginBtn;
    private TextView info;
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private OtpPresenter oPresenter;
    private ImageView forgotImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        oPresenter = new OtpPresenterImpl(this, this);
        initComponent();
        initEvent();
    }

    private void initComponent() {
        registerText = (TextView) findViewById(R.id.register_text);
        loginBtn = (Button) findViewById(R.id.login_btn);
        mobile = (EditText) findViewById(R.id.input_mobile);
        info = (TextView) findViewById(R.id.text_info_register);
        forgotImg = (ImageView) findViewById(R.id.image_login);

//        if (!PreferenceManager.getStatusAkupay()) {
//            forgotImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }
        info.setVisibility(View.GONE);
        loginBtn.setText("LUPA PIN");
        registerText.setText("LOGIN");
    }

    private void initEvent() {
        RxView.clicks(registerText).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        RxView.clicks(loginBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!TextUtils.isEmpty(mobile.getText().toString())) {
                    oPresenter.resendCode(mobile.getText().toString());
                }
            }
        });
    }

    @Override
    public void onSuccessRequest() {

    }

    @Override
    public void onSuccessResendCode(String msg) {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(OtpActivity.MOBILE, mobile.getText().toString());
        intent.putExtra(OtpActivity.IS_FORGOT, true);
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        if (progressBar.getDialog() != null && progressBar.getDialog().isShowing()) {
            progressBar.getDialog().dismiss();
        }
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
    }
}
