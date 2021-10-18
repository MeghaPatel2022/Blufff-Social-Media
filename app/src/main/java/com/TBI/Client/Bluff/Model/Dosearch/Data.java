package com.TBI.Client.Bluff.Model.Dosearch;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Data implements Serializable {

    private final static long serialVersionUID = 911241789757022006L;
    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("hastags")
    @Expose
    private List<Hastag> hastags = null;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("users", users).append("location", location).append("hastags", hastags).toString();
    }

}
