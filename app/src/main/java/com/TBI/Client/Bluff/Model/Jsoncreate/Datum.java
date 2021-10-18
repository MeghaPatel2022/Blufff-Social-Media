package com.TBI.Client.Bluff.Model.Jsoncreate;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = 6776856075235224560L;
    @SerializedName("x")
    @Expose
    private float x;
    @SerializedName("y")
    @Expose
    private float y;
    @SerializedName("username")
    @Expose
    private String username;

    public Datum(float x, float y, String username) {
        this.x = x;
        this.y = y;
        this.username = username;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "{" +
                "x:" + x +
                ", y:" + y +
                ", username:'" + username + '\'' +
                '}';
    }
}
