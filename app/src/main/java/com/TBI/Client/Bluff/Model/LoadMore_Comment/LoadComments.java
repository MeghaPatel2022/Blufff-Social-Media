package com.TBI.Client.Bluff.Model.LoadMore_Comment;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoadComments implements Serializable {

    private final static long serialVersionUID = -4633851375065965264L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("more_comments")
    @Expose
    private Integer moreComments;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getMoreComments() {
        return moreComments;
    }

    public void setMoreComments(Integer moreComments) {
        this.moreComments = moreComments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("comments", comments).append("moreComments", moreComments).toString();
    }

}
