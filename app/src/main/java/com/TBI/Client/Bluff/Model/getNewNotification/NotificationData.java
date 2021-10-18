package com.TBI.Client.Bluff.Model.getNewNotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationData {

    @SerializedName("all")
    @Expose
    private List<AllNotification> all = new ArrayList<>();
    @SerializedName("requested")
    @Expose
    private List<Requested> requested = new ArrayList<>();
    @SerializedName("suggested")
    @Expose
    private List<Suggested> suggested = new ArrayList<>();
    @SerializedName("sos")
    @Expose
    private List<So> sos = new ArrayList<>();
    @SerializedName("stars")
    @Expose
    private List<Star> stars = new ArrayList<>();

    public List<AllNotification> getAll() {
        return all;
    }

    public void setAll(List<AllNotification> all) {
        this.all = all;
    }

    public List<Requested> getRequested() {
        return requested;
    }

    public void setRequested(List<Requested> requested) {
        this.requested = requested;
    }

    public List<Suggested> getSuggested() {
        return suggested;
    }

    public void setSuggested(List<Suggested> suggested) {
        this.suggested = suggested;
    }

    public List<So> getSos() {
        return sos;
    }

    public void setSos(List<So> sos) {
        this.sos = sos;
    }

    public List<Star> getStars() {
        return stars;
    }

    public void setStars(List<Star> stars) {
        this.stars = stars;
    }


}
