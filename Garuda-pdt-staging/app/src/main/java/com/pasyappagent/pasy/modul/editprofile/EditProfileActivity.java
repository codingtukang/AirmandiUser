package com.pasyappagent.pasy.modul.editprofile;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/23/17.
 */

public class EditProfileActivity extends BaseActivity implements CommonInterface, EditProfileView{
    private EditText textName;
    private EditText textMobile;
    private EditText textEmail;
    private Button updateBtn;
    private EditProfilePresenter mPresenter;

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
    protected int getLayoutResourceId() {
        return R.layout.edit_profile_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Ubah Akun");
        textName = (EditText) findViewById(R.id.register_name);
        textMobile = (EditText) findViewById(R.id.register_mobile);
        textEmail = (EditText) findViewById(R.id.register_email);
        updateBtn = (Button) findViewById(R.id.process_register);
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new EditProfilePresenterImpl(this, this);
        GAgent agent = PreferenceManager.getAgent();
        try {
            textName.setText(agent.name);
            textMobile.setText(agent.mobile);
            textEmail.setText(agent.email);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }


        RxView.clicks(updateBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.updateProfile(textName.getText().toString(),
                        textMobile.getText().toString(),
                        textEmail.getText().toString());
            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onSuccessEdit() {
        MethodUtil.showCustomToast(this, "Profile berhasil di update", R.drawable.success);
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("openAgent", true);
        startActivity(intent);
        finish();
    }
}
