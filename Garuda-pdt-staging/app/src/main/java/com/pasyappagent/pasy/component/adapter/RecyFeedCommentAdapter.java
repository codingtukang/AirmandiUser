package com.pasyappagent.pasy.component.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GComment;
import com.pasyappagent.pasy.component.listener.ListActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedCommentAdapter extends BaseSwipeAdapter {
    private List<GComment> commentList;

    private ListActionListener itemActionListener;

    public RecyFeedCommentAdapter() {
        commentList = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.cmt_feed_row_swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, null);
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
        TextView contentTV = (TextView) convertView.findViewById(R.id.comment_content_tv);
        String sourceString = "<b>" + commentList.get(position).username + "</b> " + commentList.get(position).content;
        contentTV.setText(Html.fromHtml(sourceString));
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GComment> feeds) {
        commentList = feeds;
        notifyDataSetChanged();
    }

    public void addDataList(List<GComment> feeds) {
        if (commentList == null){
            commentList = new ArrayList<>();
        }
        commentList.addAll(feeds);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return commentList.size();
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
