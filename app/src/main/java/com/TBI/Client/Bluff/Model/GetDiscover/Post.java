package com.TBI.Client.Bluff.Model.GetDiscover;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.GetFeed.Image;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Post implements Serializable {

    private final static long serialVersionUID = 4423963399910201955L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("ulat")
    @Expose
    private String ulat;
    @SerializedName("ulang")
    @Expose
    private String ulang;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("multiple_images")
    @Expose
    private Integer multipleImages;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("more_comments")
    @Expose
    private Integer moreComments;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("bookmarked")
    @Expose
    private Integer bookmarked;
    @SerializedName("follow_post")
    @Expose
    private Integer followPost;
    @SerializedName("time_duration")
    @Expose
    private String timeDuration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUlat() {
        return ulat;
    }

    public void setUlat(String ulat) {
        this.ulat = ulat;
    }

    public String getUlang() {
        return ulang;
    }

    public void setUlang(String ulang) {
        this.ulang = ulang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getMultipleImages() {
        return multipleImages;
    }

    public void setMultipleImages(Integer multipleImages) {
        this.multipleImages = multipleImages;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getMoreComments() {
        return moreComments;
    }

    public void setMoreComments(Integer moreComments) {
        this.moreComments = moreComments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Integer bookmarked) {
        this.bookmarked = bookmarked;
    }

    public Integer getFollowPost() {
        return followPost;
    }

    public void setFollowPost(Integer followPost) {
        this.followPost = followPost;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("type", type).append("username", username).append("fullName", fullName).append("photo", photo).append("ulat", ulat).append("ulang", ulang).append("location", location).append("lat", lat).append("lang", lang).append("description", description).append("height", height).append("width", width).append("image", image).append("multipleImages", multipleImages).append("images", images).append("moreComments", moreComments).append("comments", comments).append("bookmarked", bookmarked).append("followPost", followPost).append("timeDuration", timeDuration).toString();
    }

}
