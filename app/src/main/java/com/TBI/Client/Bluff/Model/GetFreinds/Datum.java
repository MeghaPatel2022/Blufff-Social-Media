package com.TBI.Client.Bluff.Model.GetFreinds;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = 5348722469593256837L;
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
    @SerializedName("follow_by_user")
    @Expose
    private Integer followByUser;

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

    public Integer getFollowByUser() {
        return followByUser;
    }

    public void setFollowByUser(Integer followByUser) {
        this.followByUser = followByUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("username", username).append("fullName", fullName).append("photo", photo).append("followByUser", followByUser).toString();
    }

}
