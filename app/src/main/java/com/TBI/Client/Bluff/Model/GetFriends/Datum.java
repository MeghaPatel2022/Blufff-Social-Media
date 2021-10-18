package com.TBI.Client.Bluff.Model.GetFriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Datum {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<GetCloseFrend> data = null;
    @SerializedName("followers")
    @Expose
    private ArrayList<GetOtherFriends> followers = null;

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

    public ArrayList<GetCloseFrend> getData() {
        return data;
    }

    public void setData(ArrayList<GetCloseFrend> data) {
        this.data = data;
    }

    public ArrayList<GetOtherFriends> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<GetOtherFriends> followers) {
        this.followers = followers;
    }


}
