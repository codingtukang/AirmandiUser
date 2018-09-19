package com.pasyappagent.pasy.modul.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.PasyAgentApplication;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.login.passcode.PasscodeLoginActivity;
import com.pasyappagent.pasy.modul.register.RegisterActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 10/5/17.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView registerText;
    private EditText mobile;
    private Button loginBtn;
    private ImageView loginImg;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        PreferenceManager.logOut();
        initComponent();
        initEvent();
        PasyAgentApplication app = (PasyAgentApplication) getApplication();
        mFirebaseAnalytics = app.getFirebaseAnalytics();
        mFirebaseAnalytics.setCurrentScreen(this, "Login Screen", this.getClass().getSimpleName());
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "LoginActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
        mFirebaseAnalytics.setUserProperty("Page", "Login Page");
    }

    private void initComponent() {
        registerText = (TextView) findViewById(R.id.register_text);
        loginBtn = (Button) findViewById(R.id.login_btn);
        mobile = (EditText) findViewById(R.id.input_mobile);
        loginImg = (ImageView) findViewById(R.id.image_login);
//        if (!PreferenceManager.getStatusAkupay()) {
//            loginImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }
    }

    private void initEvent() {
        RxView.clicks(registerText).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        RxView.clicks(loginBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!TextUtils.isEmpty(mobile.getText().toString())) {
                    Intent intent = new Intent(LoginActivity.this, PasscodeLoginActivity.class);
                    intent.putExtra(OtpActivity.MOBILE, mobile.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
