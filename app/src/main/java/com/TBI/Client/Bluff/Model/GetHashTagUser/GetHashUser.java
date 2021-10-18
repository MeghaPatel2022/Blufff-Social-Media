package com.TBI.Client.Bluff.Model.GetHashTagUser;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetHashUser implements Serializable {

    private final static long serialVersionUID = 4111441204992728286L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("hastags")
    @Expose
    private List<Hastag> hastags = null;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

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

    public List<Hastag> getHastags() {
        return hastags;
    }

    public void setHastags(List<Hastag> hastags) {
        this.hastags = hastags;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("hastags", hastags).append("users", users).toString();
    }

}
