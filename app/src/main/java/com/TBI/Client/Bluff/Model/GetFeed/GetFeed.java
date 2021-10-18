package com.TBI.Client.Bluff.Model.GetFeed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetFeed implements Serializable {

    private final static long serialVersionUID = -1524405714523947763L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("rating")
    @Expose
    private List<Rating> rating = new ArrayList<>();
    @SerializedName("people_you_know")
    @Expose
    private List<PersonYouKnow> peopleYouKnow = new ArrayList<>();
    @SerializedName("requested_users")
    @Expose
    private List<RequestedUser> requestedUsers = new ArrayList<>();
    @SerializedName("near_by_users")
    @Expose
    private List<NearByUser> nearByUsers = new ArrayList<>();
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("stocks")
    @Expose
    private List<Stock> stocks = null;
    @SerializedName("stories")
    @Expose
    private List<Story> stories = null;
    @SerializedName("user_stories")
    @Expose
    private List<Story> userStories = null;

    public List<Story> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<Story> userStories) {
        this.userStories = userStories;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
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

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    public List<PersonYouKnow> getPeopleYouKnow() {
        return peopleYouKnow;
    }

    public void setPeopleYouKnow(List<PersonYouKnow> peopleYouKnow) {
        this.peopleYouKnow = peopleYouKnow;
    }

    public List<RequestedUser> getRequestedUsers() {
        return requestedUsers;
    }

    public void setRequestedUsers(List<RequestedUser> requestedUsers) {
        this.requestedUsers = requestedUsers;
    }

    public List<NearByUser> getNearByUsers() {
        return nearByUsers;
    }

    public void setNearByUsers(List<NearByUser> nearByUsers) {
        this.nearByUsers = nearByUsers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "GetFeed{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", posts=" + posts +
                ", stocks=" + stocks +
                ", stories=" + stories +
                ", userStories=" + userStories +
                '}';
    }
}
