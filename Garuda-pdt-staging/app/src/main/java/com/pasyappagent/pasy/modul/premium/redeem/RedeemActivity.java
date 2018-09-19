package com.pasyappagent.pasy.modul.premium.redeem;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyRewardAdapter;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GRedeem;
import com.pasyappagent.pasy.component.network.gson.GReward;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.premium.successredeem.SuccessRedeemActivity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 9/26/17.
 */

public class RedeemActivity extends BaseActivity implements RecyRewardAdapter.OnListClicked, CommonInterface, RedeemView {
    public static final String TOTAL_DOWNLINE = "downline";
    private LinearLayout chooseRedeem;
    private LinearLayout containerListRedeem;
    private TextView rewardText;
    private Button redeemBtn;
    private RecyclerView rewardList;
    private RecyRewardAdapter mAdapter;
    private List<GReward> rewards;
    private RedeemPresenter mPresenter;
    private TextView totalDownline;
    private String rewardId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.redeem_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("REDEEM REWARD");
        chooseRedeem = (LinearLayout) findViewById(R.id.choose_reward);
        containerListRedeem = (LinearLayout) findViewById(R.id.container_list_redeem);
        rewardText = (TextView) findViewById(R.id.reward_text);
        totalDownline = (TextView) findViewById(R.id.total_downline);
        redeemBtn = (Button) findViewById(R.id.redeem_btn);
        rewardList = (RecyclerView) findViewById(R.id.reward_list);
        rewardList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyRewardAdapter();
        mAdapter.setListener(this);
        rewardList.setAdapter(mAdapter);

        String downliner = getIntent().getStringExtra(TOTAL_DOWNLINE);
        totalDownline.setText(downliner);
        RxView.clicks(chooseRedeem).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (containerListRedeem.isShown()) {
                    containerListRedeem.setVisibility(View.GONE);
                } else {
                    containerListRedeem.setVisibility(View.VISIBLE);
                }

            }
        });


        RxView.clicks(redeemBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
            mPresenter.redeemReward(rewardId);
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new RedeemPresenterImpl(this, this);
        mPresenter.getReward();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void listClicked(int position, String name) {
        containerListRedeem.setVisibility(View.GONE);
        rewardText.setText(name);
        GReward reward = rewards.get(position);
        rewardId = reward.id;
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
    public void onSuccessGetReward(List<GReward> rewards) {
        mAdapter.setDataList(rewards);
        this.rewards = rewards;
    }

    @Override
    public void onSuccessRedeemReward(GRedeem redeem) {
        Intent intent = new Intent(RedeemActivity.this, SuccessRedeemActivity.class);
        intent.putExtra("itemRedeem", redeem.reward.item);
        intent.putExtra("itemId", redeem.id);
        startActivity(intent);
    }
}
