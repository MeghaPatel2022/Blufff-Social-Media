package com.TBI.Client.Bluff.Model.GetProfile;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Friend implements Serializable {

    private final static long serialVersionUID = -7993282687177796163L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("followed_by")
    @Expose
    private Integer followedBy;
    @SerializedName("follow_by_user")
    @Expose
    private Integer followByUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Integer followedBy) {
        this.followedBy = followedBy;
    }

    public Integer getFollowByUser() {
        return followByUser;
    }

    public void setFollowByUser(Integer followByUser) {
        this.followByUser = followByUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("username", username).append("fullName", fullName).append("photo", photo).append("followedBy", followedBy).append("followByUser", followByUser).toString();
    }

}
