package com.pasyappagent.pasy.modul.premium.agent;

import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GCashback;
import com.pasyappagent.pasy.component.network.gson.GCashbackRedeem;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.premium.redeem.RedeemActivity;

import rx.functions.Action1;

import static java.security.AccessController.getContext;

/**
 * Created by Dhimas on 9/26/17.
 */

public class AgentPremiumActivity extends BaseActivity implements CommonInterface, AgentPremiumView {
    private ArcProgress progressRedem;
    private LinearLayout redemHP;
    private AgentPremiumPresenter mPresenter;
    private TextView totalCashback;
    private TextView agentCashback;
    private TextView komisiAgent;
    private TextView komisiTopup;
    private TextView totalAgent;
    private LinearLayout redemMotor;
    private LinearLayout redemUmroh;
    private LinearLayout redemMobil;
    private ImageView redeemBtn;
    private int hp = 35;
    private int motor = 500;
    private int umroh = 1000;
    private int mobil = 6000;
    private int current;
    private String currentTotal = "0";
    private ImageView iconReward;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.agent_premium_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("AGEN PREMIUM");
        progressRedem = (ArcProgress) findViewById(R.id.progress_redem);
        progressRedem.setTextColor(ContextCompat.getColor(this, R.color.black));
        progressRedem.setTextSize(10);
        progressRedem.setSuffixTextPadding(0);
        progressRedem.setSuffixTextSize(10);
        progressRedem.setBottomTextSize(25);
        redemHP = (LinearLayout) findViewById(R.id.redem_handphone);
        redemMotor = (LinearLayout) findViewById(R.id.redem_motor);
        redemUmroh = (LinearLayout) findViewById(R.id.redem_umroh);
        redemMobil = (LinearLayout) findViewById(R.id.redem_mobil);
        totalCashback = (TextView) findViewById(R.id.total_cashback);
        agentCashback = (TextView) findViewById(R.id.agent_cashback);
        komisiAgent = (TextView) findViewById(R.id.komisi_agent);
        komisiTopup = (TextView) findViewById(R.id.komisi_topup);
        totalAgent = (TextView) findViewById(R.id.total_agent);
        redeemBtn = (ImageView) findViewById(R.id.redeem_btn);
        iconReward = (ImageView) findViewById(R.id.icon_reward);


        RxView.clicks(redemHP).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (current >= hp) {
                    gotoRedemActivity();
                }
            }
        });

        RxView.clicks(redemMobil).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (current >= mobil) {
                    gotoRedemActivity();
                }
            }
        });

        RxView.clicks(redemMotor).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (current >= motor) {
                    gotoRedemActivity();
                }
            }
        });

        RxView.clicks(redemUmroh).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (current >= umroh) {
                    gotoRedemActivity();
                }
            }
        });

        RxView.clicks(redeemBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentTotal.equalsIgnoreCase("0")) {
                    onFailureRequest("Total cashback belum bisa di konversi ke saldo");
                } else {
                    showDialogRedeem();
                }
            }
        });
    }

    private void showDialogRedeem() {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.content_dialog))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.token_pln:
                                mPresenter.redeemCashback();
                                dialog.dismiss();
                                break;
                            case R.id.tagihan_pln:
                                dialog.dismiss();
                                break;
                        }

                    }
                })
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(R.color.starDust_opacity_90)
                .create();
        View view = dialog.getHolderView();
        TextView title = (TextView) view.findViewById(R.id.title);
        Button first = (Button) view.findViewById(R.id.tagihan_pln);
        Button second = (Button) view.findViewById(R.id.token_pln);

        title.setText("Apakah anda ingin redeem cashback ?");
        first.setText("Tidak");
        second.setText("Ya");
        dialog.show();
    }

    private void gotoRedemActivity() {
        Intent intent = new Intent(AgentPremiumActivity.this, RedeemActivity.class);
        intent.putExtra(RedeemActivity.TOTAL_DOWNLINE, totalAgent.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new AgentPremiumPresenterImpl(this, this);
        mPresenter.getCashback();
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
    public void onSuccessGetCashback(GCashback cashback) {
        currentTotal = cashback.total_cashback;
        totalCashback.setText("RP " + MethodUtil.toCurrencyFormat(cashback.total_cashback));
        agentCashback.setText("RP " + MethodUtil.toCurrencyFormat(cashback.referral_cashback));
        komisiAgent.setText("RP " + MethodUtil.toCurrencyFormat(cashback.transaction_cashback));
        komisiTopup.setText("RP " + MethodUtil.toCurrencyFormat(cashback.topup_cashback));
    }

    @Override
    public void onSuccessGetDownline(String downline) {
        current = Integer.parseInt(downline);
        int percent;
        if (current >= mobil){
            percent = (current / mobil) * 100;
            progressRedem.setBottomText(current + "/" + mobil);
            redemHP.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemMotor.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemUmroh.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemMobil.setBackground(getDrawable(R.drawable.border_rounde_blue));
            iconReward.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_mobil_green));
        } else if (current >= umroh) {
            percent = current / umroh * 100;
            progressRedem.setBottomText(current + "/" + umroh);
            redemHP.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemMotor.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemUmroh.setBackground(getDrawable(R.drawable.border_rounde_blue));
            iconReward.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_umroh_green));
        } else if (current >= motor) {
            percent = current / motor * 100;
            progressRedem.setBottomText(current + "/" + motor);
            redemHP.setBackground(getDrawable(R.drawable.border_rounde_blue));
            redemMotor.setBackground(getDrawable(R.drawable.border_rounde_blue));
            iconReward.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_motor_green));
        } else if (current >= hp) {
            percent = current / hp * 100;
            redemHP.setBackground(getDrawable(R.drawable.border_rounde_blue));
            progressRedem.setBottomText(current + "/" + hp);
        } else {
            redemHP.setBackground(getDrawable(R.drawable.border_round_light_grey));
            percent = current / hp * 100;
            progressRedem.setBottomText(current + "/" + hp);
        }
        totalAgent.setText(downline);
        progressRedem.setProgress(percent);

    }

    @Override
    public void onSuccessRedeem(GCashbackRedeem response) {
        if (response.success) {
            MethodUtil.showCustomToast(this, "Redeem sukses", 0);
            currentTotal = "0";
            totalCashback.setText("Rp 0");
        } else {
            MethodUtil.showCustomToast(this, "Redeem gagal", R.drawable.ic_error_login);
        }

    }
}
