package com.pasyappagent.pasy.modul.feed.chats;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyChatAdapter;
import com.pasyappagent.pasy.component.adapter.RecyFeedChatPreviewAdapter;
import com.pasyappagent.pasy.component.listener.ListActionListener;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GCostumer;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLog;
import com.pasyappagent.pasy.component.network.gson.GTransferRequestLogGroup;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.customer.ContactCustomerActivity;
import com.pasyappagent.pasy.modul.requestbalance.RequestBalanceActivity;
import com.pasyappagent.pasy.modul.sendbalance.SendBalanceActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements ListActionListener, ChatPresenter.ChatView, CommonInterface {

    private RecyclerView chatList;
    private Button requestButton;
    private Button transferButton;

    private RecyChatAdapter chatAdapter;
    private String toCustomerId;
    private String toCustomerName;
    private String toAvatarBase64;
    private GCostumer toCustomer;
    private GCostumer currentCustomer;
    private ChatPresenter chatPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void setContentViewOnChild() {
        toCustomerName = getIntent().getStringExtra("name");
        toCustomerId = getIntent().getStringExtra("to_customer_id");
        toAvatarBase64 = getIntent().getStringExtra("avatar_base64");
        setChatToolbar(toAvatarBase64, toCustomerName, "Online");
        initChat();
    }

    @Override
    protected void onCreateAtChild() {
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatPresenter != null){
            chatPresenter.getLogs(toCustomerId);
        }
    }

    private void initChat() {
        chatPresenter = new ChatPresenter(this,this);
        chatList = (RecyclerView) findViewById(R.id.messages_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(layoutManager);
        requestButton = (Button) findViewById(R.id.request_btn);
        transferButton = (Button) findViewById(R.id.transfer_button);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatActivity.this, RequestBalanceActivity.class);
                intent.putExtra("dest_cust_id", toCustomer.id);
                intent.putExtra("dest_cust_phone_number", toCustomer.mobile);
                intent.putExtra("dest_cust_name", toCustomer.name);
                intent.putExtra("is_back", "true");
                startActivity(intent);
            }
        });
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, SendBalanceActivity.class);
                intent.putExtra("dest_cust_id", toCustomer.id);
                intent.putExtra("dest_cust_phone_number", toCustomer.mobile);
                intent.putExtra("dest_cust_name", toCustomer.name);
                intent.putExtra("is_back", "true");
                startActivity(intent);
            }
        });

        if (toCustomer == null) {
            toCustomer = new GCostumer();
            toCustomer.id = toCustomerId;
            toCustomer.name = toCustomerName;
        }
        chatAdapter = new RecyChatAdapter(toCustomer);
        chatAdapter.setDataList(new ArrayList<GTransferRequestLog>());
        chatAdapter.setListener(this);

        chatList.setAdapter(chatAdapter);
        chatPresenter.getLogs(toCustomerId);
    }

    @Override
    public void itemClicked(int position) {
        String refference_id = chatAdapter.feedList.get(position).refference_id;
        String activity = chatAdapter.feedList.get(position).activity;
//        if (activity.equals("request")){
//            Intent intent = new Intent(this, ChatActivity.class);
//            intent.putExtra("id", refference_id);
//            startActivity(intent);
//        }
//        else{
//
//        }

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
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
    }

    @Override
    public void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups) {

    }

    @Override
    public void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer) {
        toCustomer = to_customer;
        currentCustomer = customer;
        chatAdapter.setDataList(logs);
    }
}
