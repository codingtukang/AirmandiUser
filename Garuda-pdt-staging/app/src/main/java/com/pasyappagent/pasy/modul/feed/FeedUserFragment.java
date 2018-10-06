package com.pasyappagent.pasy.modul.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paging.listview.PagingListView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyFeedAdapter;
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.modul.feed.posts.PostDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FeedUserFragment extends Fragment implements ListActionListener {

    private PagingListView feedsList;
    private RecyFeedAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;

    public FeedUserFragment() {
        // Required empty public constructor
    }

    public static FeedUserFragment newInstance() {
        FeedUserFragment fragment = new FeedUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_feed_user, container, false);
        initFeed(v);
        return v;
    }

    private void initFeed(View view) {
        feedsAdapter = new RecyFeedAdapter();
        feedsAdapter.setListener(this);


        feedsList = (PagingListView) view.findViewById(R.id.feed_list);
        feedsList.setAdapter(feedsAdapter);

        List<GFeed> feedData = new ArrayList<>();

        feedData.addAll(loadMoreItems());
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

        feedsAdapter.setDataList(feedData);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
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

    private List<GFeed> loadMoreItems(){
        List<GFeed> feedData = new ArrayList<>();
        for(int i=0;i<5;i++){
            feedData.add(new GFeed("Name " + i,"Dummy Username","Lorem Ipsum"));
        }
        return feedData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void itemClicked(int position) {
        startActivity(new Intent(getActivity(), PostDetailActivity.class));
    }

    @Override
    public void itemDeleted(int position) {

    }
}
