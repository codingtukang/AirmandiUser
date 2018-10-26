package com.pasyappagent.pasy.modul.feed.posts;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.gson.GPostComment;
import com.pasyappagent.pasy.component.network.response.PostsResponse;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.home.HomePageActivity;

import org.w3c.dom.Text;

public class CreatePostActivity extends BaseActivity implements CommonInterface, PostView{

    private TextView postTextArea;
    private Button postButton;
    private PostPresenter mPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.post_create_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitleWithCloseButton("TULIS");
    }

    @Override
    protected void onCreateAtChild() {
        postTextArea = (TextView) findViewById(R.id.post_text_area);
        postButton = (Button) findViewById(R.id.post_btn);
        mPresenter = new PostPresenterImpl(this, this);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!postTextArea.getText().toString().equals("")){
                    mPresenter.createPost(postTextArea.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

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
    }

    @Override
    public void successFetchFeed(PostsResponse postsResponse) {
    }

    @Override
    public void successGetPostDetail(GPost post) {

    }

    @Override
    public void successAddComment(GPost post) {

    }


    @Override
    public void successDeleteComment(GPost post) {

    }

    @Override
    public void successDeletePost(String post_id) {

    }

    @Override
    public void successLikeDislikePost(GPost post) {

    }

    @Override
    public void successCreatePost(GPost post) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
