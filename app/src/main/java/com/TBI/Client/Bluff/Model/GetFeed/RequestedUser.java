package com.TBI.Client.Bluff.Model.GetFeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestedUser {

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
    private Integer followByYou;

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

    public Integer getFollowByYou() {
        return followByYou;
    }

    public void setFollowByYou(Integer followByYou) {
        this.followByYou = followByYou;
    }


}
