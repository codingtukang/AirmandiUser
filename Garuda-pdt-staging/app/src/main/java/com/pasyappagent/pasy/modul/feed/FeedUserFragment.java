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
import com.pasyappagent.pasy.component.network.gson.GFeed;

import java.util.ArrayList;
import java.util.List;

public class FeedUserFragment extends Fragment {

    private RecyclerView feedsList;
    private RecyFeedAdapter feedsAdapter;

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
        feedsList = (RecyclerView) view.findViewById(R.id.feed_list);
        feedsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedsList.setAdapter(feedsAdapter);
        List<GFeed> feedData = new ArrayList<>();
        for(int i=0;i<5;i++){
            feedData.add(new GFeed("Name " + i,"Dummy Username","Lorem Ipsum"));
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
