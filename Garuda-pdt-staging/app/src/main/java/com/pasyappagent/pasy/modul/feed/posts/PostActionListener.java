package com.pasyappagent.pasy.modul.feed.posts;

import com.pasyappagent.pasy.component.network.gson.GPost;

public interface PostActionListener {
    void likeDislikePost(GPost post);

    void sharePost(GPost post);
}
