package com.pasyappagent.pasy.component.network.response;

import com.pasyappagent.pasy.component.network.gson.GPost;
import com.pasyappagent.pasy.component.network.gson.GPostComment;

import java.util.List;

public class PostCommentResponse {
    public boolean success;
    public String message;
    public GPostComment comment;
}
