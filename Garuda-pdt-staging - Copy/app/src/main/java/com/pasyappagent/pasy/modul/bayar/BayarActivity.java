package com.pasyappagent.pasy.modul.bayar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.scanqr.ScanQRActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/5/18.
 */

public class BayarActivity extends BaseActivity implements CommonInterface, BayarView{
    private ImageView barcode;
    private BayarPresenter mPresenter;
    private ImageView scan;
    private ImageView menu;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.bayar_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        barcode = (ImageView) findViewById(R.id.barcode);
        setToolbarTitle("Bayar");
        scan = (ImageView) findViewById(R.id.scan_barcode);
        menu = (ImageView) findViewById(R.id.menu);

        RxView.clicks(scan).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                checkCamera();
            }
        });

        RxView.clicks(menu).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(BayarActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new BayarPresenterImpl(this, this);
        mPresenter.getQrcode();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
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
    public void showQrcode(String qr) {
        Glide.with(this).load(qr).dontAnimate().into(barcode);
    }

    private void checkCamera(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    startActivityForResult(new Intent(getActivity(),
//                            ScanQRActivity.class), ScanQRActivity.DIALOG_SCAN_QRCODE);
                    startActivity(new Intent(this,
                            ScanQRActivity.class));
                } else {

                    Toast.makeText(this, "Permission denied to access your camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
