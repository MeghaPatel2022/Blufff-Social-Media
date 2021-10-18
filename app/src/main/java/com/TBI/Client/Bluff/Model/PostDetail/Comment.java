package com.TBI.Client.Bluff.Model.PostDetail;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Comment implements Serializable {

    private final static long serialVersionUID = -3700514415437549167L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_duration")
    @Expose
    private String comment_duration;
    @SerializedName("liked")
    @Expose
    private Integer liked;

    public String getComment_duration() {
        return comment_duration;
    }

    public void setComment_duration(String comment_duration) {
        this.comment_duration = comment_duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }


//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("id", id).append("postId", postId).append("userId", userId).append("username", username).append("fullName", fullName).append("photo", photo).append("comment", comment).toString();
//    }

}
