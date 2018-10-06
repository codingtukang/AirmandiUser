package com.pasyappagent.pasy.modul.feed;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.paging.listview.PagingListView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyFeedAdapter;
import com.pasyappagent.pasy.component.adapter.RecyFeedChatPreviewAdapter;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.network.gson.GFeedChatPreview;

import java.util.ArrayList;
import java.util.List;

public class FeedChatFragment extends Fragment {

    private PagingListView feedsList;
    private RecyFeedChatPreviewAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;

    public FeedChatFragment() {
        // Required empty public constructor
    }

    public static FeedChatFragment newInstance() {
        FeedChatFragment fragment = new FeedChatFragment();
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
        View v = inflater.inflate(R.layout.fragment_feed_chat, container, false);
        initFeed(v);
        return v;
    }

    private void initFeed(View view) {
        feedsAdapter = new RecyFeedChatPreviewAdapter();
        feedsList = (PagingListView) view.findViewById(R.id.feed_list);
        feedsList.setAdapter(feedsAdapter);
        List<GFeedChatPreview> feedData = new ArrayList<>();

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

    private List<GFeedChatPreview> loadMoreItems(){
        List<GFeedChatPreview> feedData = new ArrayList<>();
        for(int i=0;i<5;i++){
            feedData.add(new GFeedChatPreview("Name " + i,"Dummy Message"));
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

}
