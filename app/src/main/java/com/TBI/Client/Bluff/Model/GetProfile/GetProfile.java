package com.TBI.Client.Bluff.Model.GetProfile;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.A2Z.MainDetum.AllUsers;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.Following;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.Friends;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.RecentUsers;
import com.TBI.Client.Bluff.Model.GetProfession.DatumProfession;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetProfile implements Serializable {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("others")
    @Expose
    private List<Other> others = null;
    @SerializedName("professions")
    @Expose
    private List<DatumProfession> professions = null;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("friends")
    @Expose
    private Friends friends;
    @SerializedName("following")
    @Expose
    private Following following;
    @SerializedName("all_users")
    @Expose
    private AllUsers allUsers;
    @SerializedName("recent_users")
    @Expose
    private RecentUsers recentUsers;

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

    public List<Other> getOthers() {
        return others;
    }

    public void setOthers(List<Other> others) {
        this.others = others;
    }

    public List<DatumProfession> getProfessions() {
        return professions;
    }

    public void setProfessions(List<DatumProfession> professions) {
        this.professions = professions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    public Following getFollowing() {
        return following;
    }

    public void setFollowing(Following following) {
        this.following = following;
    }

    public AllUsers getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(AllUsers allUsers) {
        this.allUsers = allUsers;
    }

    public RecentUsers getRecentUsers() {
        return recentUsers;
    }

    public void setRecentUsers(RecentUsers recentUsers) {
        this.recentUsers = recentUsers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("data", data).append("others", others).append("professions", professions).append("posts", posts).append("friends", friends).toString();
    }

}
