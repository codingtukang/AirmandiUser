package com.pasyappagent.pasy.modul.feed.posts;

import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.response.MessageResponse;
import com.pasyappagent.pasy.component.network.response.PostCommentResponse;
import com.pasyappagent.pasy.component.network.response.PostsResponse;
import com.pasyappagent.pasy.component.network.response.SinglePostResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostInteractor {
    Observable<PostsResponse> fetchFeed(int page);

    Observable<SinglePostResponse> getPostDetail(String post_id);

    Observable<SinglePostResponse> addComment(String post_id, String text);

    Observable<SinglePostResponse> deleteComment(String comment_id);

    Observable<MessageResponse> deletePost(String post_id);

    Observable<SinglePostResponse> likeDislikePost(String post_id);

    Observable<SinglePostResponse> createPost(String text);
}
