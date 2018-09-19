package com.pasyappagent.pasy.modul.register.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;

/**
 * Created by Dhimas on 10/5/17.
 */

public class PasscodeActivity extends AppCompatActivity implements EnterPasscodeFragment.OnCompleteInput,
        ReenterPasscodeFragment.OnCompleteReenterCode, CommonInterface, PasscodeView{
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private PasscodePresenter mPresenter;
    private boolean isForgot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activity);
        isForgot = getIntent().getBooleanExtra(OtpActivity.IS_FORGOT, false);
        mPresenter = new PasscodePresenterImpl(this, this);
        setEnterPasscode();
    }

    private void makeFragmentTransaction(Fragment aFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_fragment, aFragment);
        ft.commit();
    }

    private void setEnterPasscode() {
        EnterPasscodeFragment fragment = new EnterPasscodeFragment().newInstance(isForgot);
        fragment.setOnCompleteListener(this);
        makeFragmentTransaction(fragment);
    }

    private void setReenterPasscode(String code) {
        ReenterPasscodeFragment fragment = new ReenterPasscodeFragment().newInstance(code, isForgot);
        fragment.setReenterCodeListener(this);
        makeFragmentTransaction(fragment);
    }

    @Override
    public void setCode(String code) {
        setReenterPasscode(code);
    }

    @Override
    public void onMatchCode(String code) {
        mPresenter.setPasscode(code, code);
    }

    @Override
    public void onNotMatchCode() {
        setEnterPasscode();
        MethodUtil.showCustomToast(this, "Passcode tidak sesuai", R.drawable.ic_error_login);
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
    }

    @Override
    public void onSuccessResponse() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
