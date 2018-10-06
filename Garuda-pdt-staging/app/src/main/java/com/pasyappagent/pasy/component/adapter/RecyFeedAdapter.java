package com.pasyappagent.pasy.component.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.listener.ListActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedAdapter extends BaseSwipeAdapter {
    private List<GFeed> feedList;
    private ListActionListener itemActionListener;

    public RecyFeedAdapter() {
        feedList = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.user_feed_row_swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        v.findViewById(R.id.row_feed_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GFeed> feeds) {
        feedList = feeds;
        notifyDataSetChanged();
    }

    public void addDataList(List<GFeed> feeds) {
        if (feedList == null){
            feedList = new ArrayList<>();
        }
        feedList.addAll(feeds);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
