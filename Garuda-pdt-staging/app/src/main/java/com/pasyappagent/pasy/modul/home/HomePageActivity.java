package com.pasyappagent.pasy.modul.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
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
import com.pasyappagent.pasy.modul.customer.ContactCustomerActivity;
import com.pasyappagent.pasy.modul.feed.posts.PostDetailActivity;
import com.pasyappagent.pasy.modul.home.transaction.AllTransactionFragment;
import com.pasyappagent.pasy.modul.home.transaction.TransactionFragment;
import com.pasyappagent.pasy.modul.merchant.merchantlist.MerchantListActivity;
import com.pasyappagent.pasy.modul.promo.PromoActivity;
import com.pasyappagent.pasy.modul.purchase.PurchaseActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeActivity;
import com.pasyappagent.pasy.modul.scanqr.ScanQRActivity;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

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
    private FloatingActionButton chatCSFabMenu;

    //Payment & Promo Menu Container
    private RelativeLayout menuContainer;
    private View cancelMenuButtonContainer;
    private LinearLayout promoButton;

    //Payment Button
    private LinearLayout purchaseData;
    private LinearLayout purchasePulsa;
    private LinearLayout purchasePasca;
    private LinearLayout purchasePDAM;
    private LinearLayout purchaseListrik;
    private LinearLayout purchaseBpjs;
    private LinearLayout registerBpjs;
    private LinearLayout paymentTv;
    private LinearLayout paymentAsuransi;
    private LinearLayout paymentCicilan;
    private LinearLayout paymentInternet;
    private LinearLayout paymentParking;
    private LinearLayout merchant;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZopimChat.init("64QsOeI0uRiUKrWo1qO4NxG9OFdxK5TG");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.home_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        mContext = this;

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
        initPaymentMenu();
    }

    @Override
    protected void menuButtonPressed() {
        super.menuButtonPressed();
        if(menuContainer.getVisibility()==View.VISIBLE){
            menuContainer.setVisibility(View.GONE);
        }else {
            menuContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initPaymentMenu(){
        menuContainer = (RelativeLayout) findViewById(R.id.menuContainer);
        cancelMenuButtonContainer = (View) findViewById(R.id.cancelMenuButtonContainer);
        cancelMenuButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButtonPressed();
            }
        });

        promoButton = (LinearLayout) findViewById(R.id.promo);
        promoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PromoActivity.class);
                //intent.putExtra("isDeals", true);
                startActivity(intent);
            }
        });

        purchaseData = (LinearLayout) findViewById(R.id.purchase_data);
        purchaseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.INTERNET_DATA);
            }
        });

        purchasePulsa = (LinearLayout) findViewById(R.id.purchase_pulsa);
        purchasePulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.PULSA_HANDPHONE);
            }
        });
        //purchasePasca = (LinearLayout) findViewById(R.id.purchase_pasca);
        purchasePDAM = (LinearLayout) findViewById(R.id.purchase_pdam);
        purchasePDAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.PDAM);
            }
        });
        purchaseListrik = (LinearLayout) findViewById(R.id.purchase_listrik);
        purchaseListrik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuListrik();
            }
        });

        purchaseBpjs = (LinearLayout) findViewById(R.id.purchase_bpjs);
        purchaseBpjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.BPJS_KESEHATAN);
            }
        });
        //registerBpjs = (LinearLayout) findViewById(R.id.register_bpjs);
        paymentTv = (LinearLayout) findViewById(R.id.payment_tv_internet);
        paymentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.TV_INTERNET);
            }
        });
        paymentCicilan = (LinearLayout) findViewById(R.id.payment_cicilan);
        paymentCicilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.CICILAN);
            }
        });
        paymentAsuransi = (LinearLayout) findViewById(R.id.payment_asuransi);
        paymentAsuransi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.ASURANSI);
            }
        });
        merchant = (LinearLayout) findViewById(R.id.payment_merchant);
        merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MerchantListActivity.class));
            }
        });
        paymentInternet = (LinearLayout) findViewById(R.id.payment_internet);
        paymentInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPurchaseActivity(Constant.INTERNET);
            }
        });
        //paymentParking = (LinearLayout) findViewById(R.id.payment_parking);
    }

    private void initFabMenu(){
        mFab = (FloatingActionMenu) findViewById(R.id.fab_main);
        transferSaldoFabMenu = (FloatingActionButton) findViewById(R.id.transferSaldoFabMenu);
        requestSaldoFabMenu = (FloatingActionButton) findViewById(R.id.requestSaldoFabMenu);
        splitBillFabMenu = (FloatingActionButton) findViewById(R.id.splitBillFabMenu);
        tarikSaldoFabMenu = (FloatingActionButton) findViewById(R.id.tarikSaldoFabMenu);
        undangTemanFabMenu= (FloatingActionButton) findViewById(R.id.undangTemanFabMenu);
        chatCSFabMenu= (FloatingActionButton) findViewById(R.id.chatCSFabMenu);

        transferSaldoFabMenu.setLabelText("Transfer Saldo");

        transferSaldoFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ContactCustomerActivity.class);
                intent.putExtra("screen_type", "transfer");
                startActivity(intent);
            }
        });
        requestSaldoFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ContactCustomerActivity.class);
                intent.putExtra("screen_type", "request");
                startActivity(intent);
            }
        });
        chatCSFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ZopimChatActivity.class));
            }
        });
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
    protected void onSubmitBtnPressed() {

    }

    protected PaymentFragment paymentFragment;
    @Override
    public void onSuccessGetBalance(String balance) {
        this.balance = balance;
        if (paymentFragment == null){
            paymentFragment = new PaymentFragment().newInstance(balance);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_home, paymentFragment);
            ft.commit();
        }
        else{
            paymentFragment.setBalance(balance);
        }

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
        menuContainer.setVisibility(View.GONE);
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

    private void gotoPurchaseActivity(int position) {
        Intent intent = new Intent(mContext, PurchaseActivity.class);
        intent.putExtra(Constant.POSITION, position);
        startActivity(intent);
    }

    private void showMenuListrik() {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.content_dialog))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        //if (isAdded()) {
                            Intent intent = new Intent(mContext, PurchaseActivity.class);
                            switch (view.getId()) {
                                case R.id.token_pln:
                                    intent.putExtra(Constant.POSITION, Constant.LISTRIK);
                                    intent.putExtra(PurchaseActivity.TYPE_PLN, PurchaseActivity.TOKEN_PLN);
                                    startActivity(intent);
                                    break;
                                case R.id.tagihan_pln:
                                    intent.putExtra(Constant.POSITION, Constant.LISTRIK);
                                    intent.putExtra(PurchaseActivity.TYPE_PLN, PurchaseActivity.BAYAR_PLN);
                                    startActivity(intent);
                                    break;
                            }
                            dialog.dismiss();
                        //}

                    }
                })
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(R.color.starDust_opacity_90)
                .create();
        dialog.show();
    }



}
