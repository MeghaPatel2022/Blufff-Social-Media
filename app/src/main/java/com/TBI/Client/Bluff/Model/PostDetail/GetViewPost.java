package com.TBI.Client.Bluff.Model.PostDetail;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetViewPost implements Serializable {

    private final static long serialVersionUID = 312747029538843800L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("time_duration")
    @Expose
    private String time_duration;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("bookmarked")
    @Expose
    private Integer bookmarked;
    @SerializedName("more_comments")
    @Expose
    private Integer more_comments;
    @SerializedName("follow_post")
    @Expose
    private Integer follow_post;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public Integer getFollow_post() {
        return follow_post;
    }

    public void setFollow_post(Integer follow_post) {
        this.follow_post = follow_post;
    }

    public String getTime_duration() {
        return time_duration;
    }

    public void setTime_duration(String time_duration) {
        this.time_duration = time_duration;
    }

    public Integer getMore_comments() {
        return more_comments;
    }

    public void setMore_comments(Integer more_comments) {
        this.more_comments = more_comments;
    }

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Integer getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Integer bookmarked) {
        this.bookmarked = bookmarked;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("data", data).append("bookmarked", bookmarked).append("comments", comments).append("images", images).toString();
    }

}
