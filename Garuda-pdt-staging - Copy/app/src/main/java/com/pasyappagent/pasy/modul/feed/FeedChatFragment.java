package com.pasyappagent.pasy.modul.feed;

import android.content.Context;
import android.content.Intent;
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
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GFeed;
import com.pasyappagent.pasy.component.network.gson.GFeedChatPreview;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLog;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLogGroup;
import com.pasyappagent.pasy.component.network.response.PostsResponse;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.feed.chats.ChatActivity;
import com.pasyappagent.pasy.modul.feed.chats.ChatPresenter;
import com.pasyappagent.pasy.modul.feed.posts.PostDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FeedChatFragment extends Fragment implements ListActionListener, ChatPresenter.ChatView, CommonInterface {

    private PagingListView feedsList;
    private RecyFeedChatPreviewAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private ChatPresenter chatPresenter;

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
    public void onResume() {
        super.onResume();
        if (chatPresenter != null)
            loadData();
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
        chatPresenter = new ChatPresenter(this, this);
        initFeed(v);
        return v;
    }

    private void initFeed(View view) {
        feedsAdapter = new RecyFeedChatPreviewAdapter();
        feedsList = (PagingListView) view.findViewById(R.id.feed_list);
        feedsList.setAdapter(feedsAdapter);
        feedsAdapter.setListener(this);
        List<GTransferRequestLogGroup> feedData = new ArrayList<>();

        feedsList.setHasMoreItems(false);
        feedsList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {

            }
        });

        feedsAdapter.setDataList(new ArrayList<GTransferRequestLogGroup>());

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        pullToRefresh.setRefreshing(true);
        loadData();
    }

    private void loadData(){
        chatPresenter.getLogGroups();
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
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        String to_customer_id = feedsAdapter.feedList.get(position).to_customer_id;
        String name = feedsAdapter.feedList.get(position).customer.name;
        String avatarbase64 = feedsAdapter.feedList.get(position).customer.avatar_base64;
        intent.putExtra("to_customer_id", to_customer_id);
        intent.putExtra("name", name);
        intent.putExtra("avatar_base64", avatarbase64);
        startActivity(intent);
    }

    @Override
    public void itemDeleted(int position) {

    }


    @Override
    public void showProgressLoading() {
//        ((BaseActivity) getActivity()).progressBar.show(getActivity(), "", false, null);
    }

    @Override
    public void hideProgresLoading() {
//        ((BaseActivity) getActivity()).progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
    }

    @Override
    public void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups) {
        feedsAdapter.setDataList(logGroups);
        pullToRefresh.setRefreshing(false);
    }

    @Override
    public void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer) {

    }

//    @Override
//    public void successGetTransferRequestLogGroups(PostsResponse postsResponse) {
//        total_post = postsResponse.total_post;
//        if (postsResponse.posts.size() < total_post){
//            feedsList.onFinishLoading(true, null);
//        }
//        else {
//            feedsList.onFinishLoading(false, null);
//        }
//        if (currentPage == 0){
//            feedsAdapter.setDataList(postsResponse.posts);
//            currentPage = 1;
//        }else{
//            feedsAdapter.addDataList(postsResponse.posts);
//            currentPage += 1;
//        }
//        pullToRefresh.setRefreshing(false);
//    }
}
