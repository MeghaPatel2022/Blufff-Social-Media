package com.TBI.Client.Bluff.Model.GlobalSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.TBI.Client.Bluff.Model.Dosearch.Hastag;
import com.TBI.Client.Bluff.Model.Dosearch.Location;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetGlobalSearch implements Serializable {

    private final static long serialVersionUID = -6562404770529861569L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("friends")
    @Expose
    private ArrayList<Friend> friends = null;
    @SerializedName("peoples")
    @Expose
    private List<Persons> peoples = null;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("hastag")
    @Expose
    private List<Hastag> hastags = null;

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<Hastag> getHastags() {
        return hastags;
    }

    public void setHastags(List<Hastag> hastags) {
        this.hastags = hastags;
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

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public List<Persons> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<Persons> peoples) {
        this.peoples = peoples;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("friends", friends).append("peoples", peoples).append("posts", posts).toString();
    }

}
