package com.pasyappagent.pasy.modul.feed.posts;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;

public class CreatePostActivity extends BaseActivity {
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
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }
}
