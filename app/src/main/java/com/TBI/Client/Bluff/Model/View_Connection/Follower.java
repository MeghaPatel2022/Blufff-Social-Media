package com.TBI.Client.Bluff.Model.View_Connection;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Follower implements Serializable {

    private final static long serialVersionUID = -7297417632980816337L;
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
    @SerializedName("follow_by_you")
    @Expose
    private Integer followByFollowing;

    public Follower(Integer id, String username, String fullName, String photo, Integer followedBy, Integer followByFollowing) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.photo = photo;
        this.followedBy = followedBy;
        this.followByFollowing = followByFollowing;
    }

    public Integer getFollowByFollowing() {
        return followByFollowing;
    }

    public void setFollowByFollowing(Integer followByFollowing) {
        this.followByFollowing = followByFollowing;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("username", username).append("fullName", fullName).append("photo", photo).append("followedBy", followedBy).toString();
    }

}
