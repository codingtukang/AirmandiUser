package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GPagination;
import com.pasyappagent.pasy.component.network.gson.GPost;

import java.util.List;

public class PostsResponse {
    public boolean success;
    public String message;
    public int total_post;
    public List<GPost> posts;
}
