package com.TBI.Client.Bluff.Model.PostDetail;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TagPerson implements Serializable {

    private final static long serialVersionUID = -1308469507845438490L;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("y")
    @Expose
    private String y;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("username", username).append("x", x).append("y", y).toString();
    }

}
