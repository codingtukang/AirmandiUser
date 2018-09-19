package com.pasyappagent.pasy.modul.feed;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyFeedAdapter;
import com.pasyappagent.pasy.component.adapter.RecyFeedChatPreviewAdapter;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.network.gson.GFeedChatPreview;

import java.util.ArrayList;
import java.util.List;

public class FeedChatFragment extends Fragment {

    private RecyclerView feedsList;
    private RecyFeedChatPreviewAdapter feedsAdapter;

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
        feedsList = (RecyclerView) view.findViewById(R.id.feed_list);
        feedsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedsList.setAdapter(feedsAdapter);
        List<GFeedChatPreview> feedData = new ArrayList<>();
        for(int i=0;i<5;i++){
            feedData.add(new GFeedChatPreview("Name " + i,"Dummy Message"));
        }
        feedsAdapter.setDataList(feedData);

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
