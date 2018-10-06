package com.pasyappagent.pasy.modul.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyFeedAdapter;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.baznas.BaznasActivity;
import com.pasyappagent.pasy.modul.feed.FeedPagerAdapter;
import com.pasyappagent.pasy.modul.feed.posts.CreatePostActivity;
import com.pasyappagent.pasy.modul.merchant.merchantlist.MerchantListActivity;
import com.pasyappagent.pasy.modul.promo.PromoActivity;
import com.pasyappagent.pasy.modul.purchase.PurchaseActivity;
import com.pasyappagent.pasy.modul.scanqr.ScanQRActivity;
import com.pasyappagent.pasy.modul.topup.topup.TopupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by Dhimas on 9/20/17.
 */

public class PaymentFragment extends Fragment{
    private Button topupBtn;
    private Button bayarBtn;
    private Button pulsaBtn;
    private Button plnBtn;
    private Button dealsBtn;
    private Button createPostBtn;
    private LinearLayout containerBaznas;
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
//    private LinearLayout bayar;
    private LinearLayout merchant;
    private TextView balanceText;
    private TextView name;
    private TextView phone;
    private TextView point;
//    private ImageView scan;
    public static final String BALANCE = "balance";
    private LinearLayout memberNumberContainer;
    private TextView memberNo;
    private SwipeRefreshLayout pullRefresh;

    //Feed Variable
    private ViewPager feedPager;
    private FeedPagerAdapter feedPagerAdapter;
    private ImageView feedPagerIndicatorImage;
    private Button userTabButton;
    private Button chatTabButton;
    private Button trxTabButton;

    Context mContext;

    public PaymentFragment newInstance(String balance) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BALANCE, balance);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_fragment_layout, container, false);
        mContext = getContext();
        initComponent(view);
        initEvent();




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initComponent(View view) {
        memberNumberContainer = (LinearLayout) view.findViewById(R.id.member_number_container);
        memberNo = (TextView) view.findViewById(R.id.member_no);
        topupBtn = (Button) view.findViewById(R.id.topup_btn);
        bayarBtn = (Button) view.findViewById(R.id.bayar_btn);
        pulsaBtn = (Button) view.findViewById(R.id.pulsa_btn);
        plnBtn = (Button) view.findViewById(R.id.pln_btn);
        dealsBtn = (Button) view.findViewById(R.id.deal_btn);
        createPostBtn = (Button) view.findViewById(R.id.create_post_btn);
        containerBaznas = (LinearLayout) view.findViewById(R.id.baznas);
        purchaseData = (LinearLayout) view.findViewById(R.id.purchase_data);
        purchasePulsa = (LinearLayout) view.findViewById(R.id.purchase_pulsa);
        purchasePasca = (LinearLayout) view.findViewById(R.id.purchase_pasca);
        purchasePDAM = (LinearLayout) view.findViewById(R.id.purchase_pdam);
        purchaseListrik = (LinearLayout) view.findViewById(R.id.purchase_listrik);
        purchaseBpjs = (LinearLayout) view.findViewById(R.id.purchase_bpjs);
        registerBpjs = (LinearLayout) view.findViewById(R.id.register_bpjs);
        paymentTv = (LinearLayout) view.findViewById(R.id.payment_tv_internet);
        paymentCicilan = (LinearLayout) view.findViewById(R.id.payment_cicilan);
        paymentAsuransi = (LinearLayout) view.findViewById(R.id.payment_asuransi);
        merchant = (LinearLayout) view.findViewById(R.id.payment_merchant);
        paymentInternet = (LinearLayout) view.findViewById(R.id.payment_internet);
        paymentParking = (LinearLayout) view.findViewById(R.id.payment_parking);
//        scan = (ImageView) view.findViewById(R.id.scan_barcode);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        point = (TextView) view.findViewById(R.id.point);
        balanceText = (TextView) view.findViewById(R.id.balance);
//        bayar = (LinearLayout) view.findViewById(R.id.bayar);
        String balance = getArguments().getString(BALANCE);
        balanceText.setText("Rp " + MethodUtil.toCurrencyFormat(balance));

        pullRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        pullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                startActivity(getActivity().getIntent());
                pullRefresh.setRefreshing(false);
            }
        });
        pullRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Setup Pager
        feedPager = (ViewPager) view.findViewById(R.id.feedPager);
        feedPagerIndicatorImage = (ImageView) view.findViewById(R.id.feedPagerIndicatorImage);
        feedPagerAdapter = new FeedPagerAdapter(getActivity().getSupportFragmentManager());
        feedPager.setAdapter(feedPagerAdapter);

        feedPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 : {
                        feedPagerIndicatorImage.setImageDrawable(getResources().getDrawable(R.drawable.indicator_tab_1));
                    }break;
                    case 1 : {
                        feedPagerIndicatorImage.setImageDrawable(getResources().getDrawable(R.drawable.indicator_tab_2));
                    }break;
                    case 2 : {
                        feedPagerIndicatorImage.setImageDrawable(getResources().getDrawable(R.drawable.indicator_tab_3));
                    }break;
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        userTabButton = (Button) view.findViewById(R.id.userTabButton);
        chatTabButton = (Button) view.findViewById(R.id.chatTabButton);
        trxTabButton = (Button) view.findViewById(R.id.trxTabButton);

        userTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedPager.setCurrentItem(0);
            }
        });
        chatTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedPager.setCurrentItem(1);
            }
        });
        trxTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedPager.setCurrentItem(2);
            }
        });
    }



    private void initEvent() {
        GAgent user = PreferenceManager.getAgent();
        if (user != null) {
            if (user.name != null) {
                name.setText(user.name);
            }
            if (user.mobile != null) {
                phone.setText(user.mobile);
            }
            if (user.points != null) {
                point.setText(user.points);
            }
            if (user.id != null) {
                int userNumb = Integer.parseInt(user.id);
                if (userNumb <= 100) {
                    memberNumberContainer.setVisibility(View.VISIBLE);
                    memberNo.setText(userNumb + "");
                }
            }
        }



//        RxView.clicks(scan).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                checkCamera();
//            }
//        });

        RxView.clicks(paymentParking).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                checkCamera();
            }
        });

        RxView.clicks(bayarBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //startActivity(new Intent(mContext, BayarActivity.class));
                checkCamera();
            }
        });

        RxView.clicks(pulsaBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.PULSA_HANDPHONE);
            }
        });

        RxView.clicks(plnBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showMenuListrik();
            }
        });

        RxView.clicks(dealsBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), PromoActivity.class);
                intent.putExtra("isDeals", true);
                startActivity(intent);
            }
        });

        RxView.clicks(topupBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), TopupActivity.class));
            }
        });

        RxView.clicks(containerBaznas).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), BaznasActivity.class));
            }
        });

        RxView.clicks(purchaseData).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.INTERNET_DATA);
            }
        });
        RxView.clicks(purchasePulsa).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.PULSA_HANDPHONE);
            }
        });

        RxView.clicks(paymentTv).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.TV_INTERNET);
            }
        });

        RxView.clicks(createPostBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), CreatePostActivity.class));
            }
        });

        RxView.clicks(merchant).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), MerchantListActivity.class));
            }
        });

        RxView.clicks(paymentInternet).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.INTERNET);
            }
        });

        RxView.clicks(paymentAsuransi).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.ASURANSI);
            }
        });

        RxView.clicks(purchasePasca).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });

        RxView.clicks(purchasePDAM).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.PDAM);
            }
        });

        RxView.clicks(purchaseListrik).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showMenuListrik();
            }
        });

        RxView.clicks(purchaseBpjs).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.BPJS_KESEHATAN);
            }
        });

         RxView.clicks(registerBpjs).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {

                }
        });

        RxView.clicks(paymentCicilan).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoPurchaseActivity(Constant.CICILAN);
            }
        });

//        RxView.clicks(bayar).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                startActivity(new Intent(getActivity(), BayarActivity.class));
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 199: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    startActivityForResult(new Intent(getActivity(),
//                            ScanQRActivity.class), ScanQRActivity.DIALOG_SCAN_QRCODE);
//                    startActivity(new Intent(getActivity(),
//                            ScanQRActivity.class));
                    String transactionParkingId = PreferenceManager.getParkingId();
                } else {

                    Toast.makeText(getActivity(), "Permission denied to access your camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void checkCamera(){
        //requestPermissions(new String[]{Manifest.permission.CAMERA}, 199);


        //startActivity(new Intent(mContext,ScanQRActivity.class));

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            if (requestCode == ScanQRActivity.DIALOG_SCAN_QRCODE) {
                String barscanresult = data.getExtras().getString(ScanQRActivity.RESULT_READ_QRCODE);
                boolean isTransaction = data.getExtras().getBoolean(ScanQRActivity.IS_TRANSACTION, false);
                if (isTransaction) {
                    try {
                        JSONObject obj = new JSONObject(barscanresult);
//                        mPresenter.getQRData(obj.get("code").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(getActivity(), "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoPurchaseActivity(int position) {
        Intent intent = new Intent(getActivity(), PurchaseActivity.class);
        intent.putExtra(Constant.POSITION, position);
        startActivity(intent);
    }

    private void showMenuListrik() {
        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.content_dialog))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (isAdded()) {
                            Intent intent = new Intent(getActivity(), PurchaseActivity.class);
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
                        }

                    }
                })
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(R.color.starDust_opacity_90)
                .create();
        dialog.show();
    }
}
