package com.pasyappagent.pasy.modul.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.PasyAgentApplication;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.bayar.BayarActivity;
import com.pasyappagent.pasy.modul.chat.ChatActivity;
import com.pasyappagent.pasy.modul.checkpasscode.CheckPasscodeActivity;
import com.pasyappagent.pasy.modul.home.transaction.AllTransactionFragment;
import com.pasyappagent.pasy.modul.home.transaction.TransactionFragment;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeActivity;
import com.pasyappagent.pasy.modul.scanqr.ScanQRActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.transfuse.annotations.Bind;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.functions.Action1;

import static com.pasyappagent.pasy.modul.register.otp.OtpActivity.IS_FORGOT;

/**
 * Created by Dhimas on 9/19/17.
 */

public class HomePageActivity extends BaseActivity implements HomeView, CommonInterface{

//    private SectionsPagerAdapter pagerAdapter;
    private HomePresenter mPresenter;
    private String balance;
    private boolean isPremium;
//    private ImageView chat;
//    private RelativeLayout notif;
//    private TextView countTxt;
//    private ViewPager mViewPager;
//    private SwipeRefreshLayout pullRefresh;
    private ImageView bayar;
    private ImageView scan;
    private Socket mSocket;
    private int countChat;

    //Fab Floating Button Action
    private FloatingActionMenu mFab;
    private FloatingActionButton transferSaldoFabMenu;
    private FloatingActionButton requestSaldoFabMenu;
    private FloatingActionButton splitBillFabMenu;
    private FloatingActionButton tarikSaldoFabMenu;
    private FloatingActionButton undangTemanFabMenu;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.home_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        scan = (ImageView) findViewById(R.id.scan_barcode);
        bayar = (ImageView) findViewById(R.id.bayar);

        RxView.clicks(bayar).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(HomePageActivity.this, BayarActivity.class));
            }
        });

        RxView.clicks(scan).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                checkCamera();
            }
        });


        setSlidingMenu(this);
        final String[] user = PreferenceManager.getUser();

        final Handler mTypingHandler = new Handler();
        PasyAgentApplication app = (PasyAgentApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on("chat message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                countChat++;
                mTypingHandler.postDelayed(onTypingTimeout, 1000);
            }
        });
        mSocket.connect();
        FirebaseAnalytics mFirebaseAnalytics = app.getFirebaseAnalytics();
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "HomeActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
        mFirebaseAnalytics.setUserProperty("Page", "home Page");

        RxView.clicks(changePassode).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(HomePageActivity.this, CheckPasscodeActivity.class);
                String[] user = PreferenceManager.getUser();
                intent.putExtra(OtpActivity.MOBILE, user[1]);
                startActivityForResult(intent, CHANGE_PASSCODE);
            }
        });

        initFabMenu();
    }

    private void initFabMenu(){
        mFab = (FloatingActionMenu) findViewById(R.id.fab_main);
        transferSaldoFabMenu = (FloatingActionButton) findViewById(R.id.transferSaldoFabMenu);
        requestSaldoFabMenu = (FloatingActionButton) findViewById(R.id.requestSaldoFabMenu);
        splitBillFabMenu = (FloatingActionButton) findViewById(R.id.splitBillFabMenu);
        tarikSaldoFabMenu = (FloatingActionButton) findViewById(R.id.tarikSaldoFabMenu);
        undangTemanFabMenu= (FloatingActionButton) findViewById(R.id.undangTemanFabMenu);

        transferSaldoFabMenu.setLabelText("Transfer Saldo");
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
//            countTxt.setText(countChat + "");
//            notif.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreateAtChild() {
        mPresenter = new HomePresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onSuccessGetBalance(String balance) {
        this.balance = balance;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_home, new PaymentFragment().newInstance(balance));
        ft.commit();
    }

    @Override
    public void onSuccessCheckPremium(Boolean isPremium) {
        this.isPremium = isPremium;
//        setHomeAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countChat = 0;
        mPresenter.getDataHomeActivity();
//        notif.setVisibility(View.GONE);
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

    private void setHomeAdapter() {
//        pagerAdapter.removeTabFragment();
//        pagerAdapter.addTabFragment(new PaymentFragment().newInstance(balance));
//        pagerAdapter.addTabFragment(new TransactionFragment().newInstance());
//        pagerAdapter.addTabFragment(new AllTransactionFragment().newInstance());
//        pagerAdapter.addTabFragment(new AgentFragment().newInstance(isPremium));

        boolean isOpenAgent = getIntent().getBooleanExtra("openAgent", false);
        if (isOpenAgent) {
//            mViewPager.setCurrentItem(2, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHANGE_PASSCODE) {
                Intent intent = new Intent(HomePageActivity.this, PasscodeActivity.class);
                intent.putExtra(IS_FORGOT, true);
                startActivity(intent);
            }
        }
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
                    startActivity(new Intent(HomePageActivity.this,
                            ScanQRActivity.class));
                } else {
                    Toast.makeText(HomePageActivity.this, "Permission denied to access your camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
