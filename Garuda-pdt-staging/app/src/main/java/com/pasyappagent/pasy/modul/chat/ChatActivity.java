package com.pasyappagent.pasy.modul.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.PasyAgentApplication;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GLogo;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * Created by Dhimas on 12/21/17.
 */

public class ChatActivity extends BaseActivity implements CommonInterface, ChatView{
    private FrameLayout container;
    private ChatPresenter mPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.chat_activity_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Chat Online");
        mPresenter = new ChatPresenterImpl(this, this);
//        mPresenter.uploadChat();
        container = (FrameLayout) findViewById(R.id.container_chat);
        ChatFragment fragment = new ChatFragment();
        makeFragmentTransaction(fragment);
    }

    private void makeFragmentTransaction(Fragment aFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_chat, aFragment);
        ft.commit();
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onSuccessUploadChat(GLogo logo) {
        PasyAgentApplication app = (PasyAgentApplication) getApplication();
        Socket mSocket = app.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("url", logo.base_url + "/" + logo.path);
            mSocket.emit("chat image", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }
}
