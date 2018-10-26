package com.pasyappagent.pasy.modul.feed.posts;

import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.gson.GPostComment;
import com.pasyappagent.pasy.component.network.response.PostsResponse;

import java.util.List;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostView {
    void successFetchFeed(PostsResponse postsResponse);

    void successGetPostDetail(GPost post);

    void successAddComment(GPost post);

    void successDeleteComment(GPost post);

    void successDeletePost(String post_id);

    void successLikeDislikePost(GPost post);

    void successCreatePost(GPost post);
}
