package com.TBI.Client.Bluff.Model.GetFeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByUser {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("profession_id")
    @Expose
    private Integer professionId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("follow_by_user")
    @Expose
    private Integer followByUser;
    @SerializedName("distance_in_km")
    @Expose
    private Double distanceInKm;

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

    public Integer getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Integer professionId) {
        this.professionId = professionId;
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

    public Double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(Double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }


}
