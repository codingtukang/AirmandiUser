package com.pasyappagent.pasy.modul.feed.posts;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.paging.listview.PagingListView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyFeedCommentAdapter;
import com.pasyappagent.pasy.component.network.gson.GComment;
import com.pasyappagent.pasy.component.listener.ListActionListener;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity implements ListActionListener {

    private PagingListView feedsList;
    private RecyFeedCommentAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.post_detail_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("POST");
        initComment();
    }

    @Override
    protected void onCreateAtChild() {
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    private void initComment() {
        feedsAdapter = new RecyFeedCommentAdapter();
        feedsAdapter.setListener(this);


        feedsList = (PagingListView) findViewById(R.id.comment_list);
        feedsList.setAdapter(feedsAdapter);

        List<GComment> commentData = new ArrayList<>();

        commentData.addAll(loadMoreItems());
        feedsList.setHasMoreItems(true);
        feedsList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        feedsList.onFinishLoading(true, null);
                        feedsAdapter.addDataList(loadMoreItems());
                    }
                }, 3000);
            }
        });

        feedsAdapter.setDataList(commentData);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                    }
                }, 1000);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private List<GComment> loadMoreItems(){
        List<GComment> feedData = new ArrayList<>();
        for(int i=0;i<5;i++){
            feedData.add(new GComment("Name " + i,"Dummy Username","Lorem Ipsum"));
        }
        return feedData;
    }

    @Override
    public void itemClicked(int position) {
    }

    @Override
    public void itemDeleted(int position) {

    }
}