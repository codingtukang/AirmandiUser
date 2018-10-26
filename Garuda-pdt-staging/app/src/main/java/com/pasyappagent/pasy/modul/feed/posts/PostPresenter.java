package com.pasyappagent.pasy.modul.feed.posts;

import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.response.PostCommentResponse;
import com.pasyappagent.pasy.component.network.response.PostsResponse;
import com.pasyappagent.pasy.component.network.response.SinglePostResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostPresenter {
    void fetchFeed(int page);

    void getPostDetail(String post_id);

    void addComment(String post_id, String text);

    void deleteComment(String comment_id);

    void deletePost(String post_id);

    void likeDislikePost(String post_id);

    void createPost(String text);
}
