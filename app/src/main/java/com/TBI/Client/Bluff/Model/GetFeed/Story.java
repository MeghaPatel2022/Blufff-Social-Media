package com.TBI.Client.Bluff.Model.GetFeed;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Story implements Serializable {

    private final static long serialVersionUID = -4372802818405206221L;
    @SerializedName("followed_by")
    @Expose
    private Integer followedBy;
    @SerializedName("user_id")
    @Expose
    private Integer id;
    @SerializedName("all_seen")
    @Expose
    private Integer all_seen;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("images")
    @Expose
    private List<Image_> images = null;

    public Integer getAll_seen() {
        return all_seen;
    }

    public void setAll_seen(Integer all_seen) {
        this.all_seen = all_seen;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Integer followedBy) {
        this.followedBy = followedBy;
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

    public List<Image_> getImages() {
        return images;
    }

    public void setImages(List<Image_> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Story{" +
                "followedBy=" + followedBy +
                ", id=" + id +
                ", all_seen=" + all_seen +
                ", position=" + position +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", photo='" + photo + '\'' +
                ", images=" + images +
                '}';
    }
}
