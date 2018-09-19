package com.pasyappagent.pasy.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GReward;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedAdapter extends RecyclerView.Adapter<RecyFeedAdapter.ViewHolder>{
    private List<GFeed> feedList;
    private OnListClicked listClickedListener;

    public RecyFeedAdapter() {
        feedList = new ArrayList<>();
    }

    public void setListener(OnListClicked listClicked) {
        this.listClickedListener = listClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyFeedAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GFeed feed = feedList.get(position);
//        if (TextUtils.isEmpty(feed.item) && !TextUtils.isEmpty(feed.cash_back)) {
//            holder.rewardName.setText("SALDO TOP UP Rp. " + MethodUtil.toCurrencyFormat(reward.cash_back));
//        } else {
//            holder.rewardName.setText(reward.item);
//        }
//
//        if (reward.logo != null) {
//            Glide.with(holder.itemView.getContext()).load(reward.logo.base_url + reward.logo.path)
//            .dontAnimate().into(holder.rewardIcon);
//        }
//
//        RxView.clicks(holder.rewardContainer).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                if (listClickedListener != null) {
//                    listClickedListener.listClicked(position, holder.rewardName.getText().toString());
//                }
//            }
//        });


    }

    public void setDataList(List<GFeed> feeds) {
        feedList = feeds;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
//        private LinearLayout rewardContainer;
//        private ImageView rewardIcon;
//        private TextView rewardName;

        private ViewHolder(View view) {
            super(view);
//            rewardContainer = (LinearLayout) view.findViewById(R.id.container_reward_list);
//            rewardIcon = (ImageView) view.findViewById(R.id.reward_icon);
//            rewardName = (TextView) view.findViewById(R.id.reward_text);
        }
    }

    public interface OnListClicked{
        void listClicked(int position, String name);
    }
}
