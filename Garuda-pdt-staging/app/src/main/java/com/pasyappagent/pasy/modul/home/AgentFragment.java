package com.pasyappagent.pasy.modul.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.checkpasscode.CheckPasscodeActivity;
import com.pasyappagent.pasy.modul.editprofile.EditProfileActivity;
import com.pasyappagent.pasy.modul.faq.FaqActivity;
import com.pasyappagent.pasy.modul.login.LoginActivity;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumActivity;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumPresenter;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumPresenterImpl;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumView;
import com.pasyappagent.pasy.modul.premium.register.RegisterPremiumAgentActivity;
import com.pasyappagent.pasy.modul.premium.tutorial.TutorialPremiumActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenter;
import com.pasyappagent.pasy.modul.register.otp.OtpPresenterImpl;
import com.pasyappagent.pasy.modul.register.otp.OtpView;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeActivity;
import com.pasyappagent.pasy.modul.sendbalance.SendBalanceActivity;
import com.pasyappagent.pasy.modul.termcondition.TermConditionActivity;

import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;
import static com.pasyappagent.pasy.modul.register.otp.OtpActivity.IS_FORGOT;

/**
 * Created by Dhimas on 9/25/17.
 */

public class AgentFragment extends Fragment implements CommonInterface, AgentPremiumView {
    private static final String IS_PREMIUM = "isPremium";
    private RelativeLayout agentPremium;
    private RelativeLayout logout;
    private TextView agentName;
    private TextView agentMobile;
    private RelativeLayout sendBalance;
    private RelativeLayout changePin;
    private RelativeLayout editProfile;
    private RelativeLayout terms;
    private RelativeLayout faq;
    private Button premiumBtn;
    private LinearLayout premiumContainer;
    private TextView refferalCode;
    private Button codeRefferalCopy;
    private ImageView starPremium;
    private LinearLayout containerCashback;
    private TextView amountCashback;
    private AgentPremiumPresenter mPresenter;
    private CustomProgressBar progressBar = new CustomProgressBar();
    private boolean isGetCashback;
    private static final int CHECK_PASSCODE = 88;

    public AgentFragment newInstance(boolean isPremium) {
        AgentFragment fragment = new AgentFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_PREMIUM, isPremium);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agent_fragment_layout, container, false);
        mPresenter = new AgentPremiumPresenterImpl(this, this);
        mPresenter.getCashback();
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        agentPremium = (RelativeLayout) view.findViewById(R.id.agent_premium);
        logout = (RelativeLayout) view.findViewById(R.id.logout);
        agentName = (TextView) view.findViewById(R.id.agent_name);
        agentMobile = (TextView) view.findViewById(R.id.agent_mobile);
        sendBalance = (RelativeLayout) view.findViewById(R.id.send_balance);
        premiumBtn = (Button) view.findViewById(R.id.premium_button);
        premiumContainer = (LinearLayout) view.findViewById(R.id.container_premium);
        refferalCode = (TextView) view.findViewById(R.id.refferal_code);
        codeRefferalCopy = (Button) view.findViewById(R.id.copy_refferal_code);
        starPremium = (ImageView) view.findViewById(R.id.star_premium);
        changePin = (RelativeLayout) view.findViewById(R.id.change_pin);
        editProfile = (RelativeLayout) view.findViewById(R.id.edit_profile);
        containerCashback = (LinearLayout) view.findViewById(R.id.container_cashback);
        amountCashback = (TextView) view.findViewById(R.id.amount_cashback);
        terms = (RelativeLayout) view.findViewById(R.id.terms);
        faq = (RelativeLayout) view.findViewById(R.id.faq);

        final GAgent gAgent = PreferenceManager.getAgent();

        agentName.setText(gAgent.name);
        agentMobile.setText(gAgent.mobile);
        refferalCode.setText(gAgent.code);

        final boolean isPremium = getArguments().getBoolean(IS_PREMIUM);

        if (isPremium) {
            premiumBtn.setVisibility(View.GONE);
            premiumContainer.setVisibility(View.VISIBLE);
            starPremium.setVisibility(View.VISIBLE);
            containerCashback.setVisibility(View.GONE
            );
        }


        RxView.clicks(agentPremium).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), TutorialPremiumActivity.class);
                intent.putExtra(TutorialPremiumActivity.PREMIUM_USER, true);
                startActivity(intent);
            }
        });

        RxView.clicks(logout).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                gotoLoginPage();
            }
        });

        RxView.clicks(containerCashback).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isGetCashback) {
                    showPopupPremium();
                }
            }
        });

        RxView.clicks(sendBalance).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SendBalanceActivity.class));
            }
        });

        RxView.clicks(premiumBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), TutorialPremiumActivity.class);
                intent.putExtra(TutorialPremiumActivity.PREMIUM_USER, false);
                startActivity(intent);
            }
        });

        RxView.clicks(faq).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), FaqActivity.class));
            }
        });

        RxView.clicks(codeRefferalCopy).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", gAgent.code);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Kode telah dicopy", Toast.LENGTH_SHORT).show();
            }
        });

        RxView.clicks(changePin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CheckPasscodeActivity.class);
                String[] user = PreferenceManager.getUser();
                intent.putExtra(OtpActivity.MOBILE, user[1]);
                startActivityForResult(intent, CHECK_PASSCODE);
            }
        });

        RxView.clicks(editProfile).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        RxView.clicks(terms).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), TermConditionActivity.class));
            }
        });
    }

    private void gotoLoginPage() {
        PreferenceManager.logOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(getActivity(), PasscodeActivity.class);
            intent.putExtra(IS_FORGOT, true);
            startActivity(intent);
        }
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(getContext(), "", false, null);
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
        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            PreferenceManager.logOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onSuccessGetCashback(GCashback cashback) {
        if (!cashback.total_cashback.equalsIgnoreCase("0")) {
            isGetCashback = true;
        }
        String total = "Rp. "+ MethodUtil.toCurrencyFormat(cashback.total_cashback);
        amountCashback.setText(total);
    }

    @Override
    public void onSuccessGetDownline(String downline) {

    }

    @Override
    public void onSuccessRedeem(GCashbackRedeem json) {

    }

    private void showPopupPremium() {
        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.content_dialog))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.token_pln:
                                Intent intent = new Intent(getActivity(), TutorialPremiumActivity.class);
                                intent.putExtra(TutorialPremiumActivity.PREMIUM_USER, false);
                                startActivity(intent);
                                break;
                            case R.id.tagihan_pln:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(R.color.starDust_opacity_90)
                .create();
        View view = dialog.getHolderView();
        TextView title = (TextView) view.findViewById(R.id.title);
        Button first = (Button) view.findViewById(R.id.tagihan_pln);
        Button second = (Button) view.findViewById(R.id.token_pln);

        title.setText("Daftar menjadi agen premium untuk mendapatkan cashback?");
        first.setText("Tidak");
        second.setText("Ya");
        dialog.show();
    }
}
