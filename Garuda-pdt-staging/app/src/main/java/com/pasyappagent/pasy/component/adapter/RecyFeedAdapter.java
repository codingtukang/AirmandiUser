package com.pasyappagent.pasy.component.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.modul.feed.posts.PostActionListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedAdapter extends BaseSwipeAdapter {
    public List<GPost> feedList;
    private ListActionListener itemActionListener;
    private PostActionListener postActionListener;

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
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        v.findViewById(R.id.row_feed_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemDeleted(position);
                swipeLayout.close();
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        final GPost post = feedList.get(position);
        final TextView customer_name = convertView.findViewById(R.id.customer_name);
        TextView customer_username = convertView.findViewById(R.id.customer_username);
        TextView likes_count = convertView.findViewById(R.id.likes_count);
        TextView comments_count = convertView.findViewById(R.id.comments_count);
        TextView post_text = convertView.findViewById(R.id.post_text);
        CircleImageView avatar_img = convertView.findViewById(R.id.avatar_img);
        ImageView shareButton = convertView.findViewById(R.id.share_button);

        post_text.setText(post.text);
        customer_name.setText(post.customer.name);
        customer_username.setText(post.customer.username);
        likes_count.setText((post.likes_count != null ? " " + post.likes_count : ""));
        comments_count.setText((post.comments_count != null ? " " + post.comments_count : ""));
        if (post.customer != null && post.customer.avatar_base64 != null && !post.customer.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(post.customer.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar_img.setImageBitmap(decodedByte);
        }

        likes_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postActionListener.likeDislikePost(post);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareContent(post.customer.name+" on Pampassy: "+post.text);
                postActionListener.sharePost(post);
            }
        });
    }

    private void shareContent(String text){
//        this.itemActionListener.itemClicked();
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GPost> posts) {
        feedList = posts;
        notifyDataSetChanged();
    }

    public void deleteItem(String post_id) {
        int index = 0;
        for (GPost post : feedList) {
            if (post.id.equals(post_id)){
                break;
            }
            index++;
        }
        feedList.remove(index);
        notifyDataSetChanged();
    }

    public void replaceItem(String post_id, GPost newPost) {
        int index = 0;
        for (GPost post : feedList) {
            if (post.id.equals(post_id)){
                break;
            }
            index++;
        }
        feedList.get(index).likes_count = ((newPost.likes_count != null) ? " " + newPost.likes_count : "");
        notifyDataSetChanged();
    }

    public void addDataList(List<GPost> posts) {
        if (feedList == null){
            feedList = new ArrayList<>();
        }
        feedList.addAll(posts);
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

    public void setActionListener(PostActionListener listClicked) {
        this.postActionListener = listClicked;
    }

}
