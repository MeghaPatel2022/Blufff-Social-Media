package com.TBI.Client.Bluff.Model.SearchUser;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.View_Connection.Follower;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.Model.View_Connection.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchUser implements Serializable {

    private final static long serialVersionUID = 1177008179076480376L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("followers")
    @Expose
    private List<Follower> followers = null;
    @SerializedName("following")
    @Expose
    private List<Following> following = null;
    @SerializedName("requested")
    @Expose
    private List<Request> requested = null;

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

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowing() {
        return following;
    }

    public void setFollowing(List<Following> following) {
        this.following = following;
    }

    public List<Request> getRequested() {
        return requested;
    }

    public void setRequested(List<Request> requested) {
        this.requested = requested;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("data", data).toString();
    }

}
