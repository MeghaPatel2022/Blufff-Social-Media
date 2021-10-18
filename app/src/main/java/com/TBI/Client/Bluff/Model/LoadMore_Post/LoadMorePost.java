package com.TBI.Client.Bluff.Model.LoadMore_Post;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoadMorePost implements Serializable {

    private final static long serialVersionUID = 5890934370892762332L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("posts", posts).toString();
    }

}
