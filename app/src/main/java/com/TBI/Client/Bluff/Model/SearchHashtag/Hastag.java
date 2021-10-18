package com.TBI.Client.Bluff.Model.SearchHashtag;


import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Hastag implements Serializable {

    private final static long serialVersionUID = -2799751385553078430L;
    @SerializedName("hashtag")
    @Expose
    private String hashtag;
    @SerializedName("total_posts")
    @Expose
    private String total_posts;
    @SerializedName("id")
    @Expose
    private int id;

    public String getTotal_posts() {
        return total_posts;
    }

    public void setTotal_posts(String total_posts) {
        this.total_posts = total_posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("hashtag", hashtag).toString();
    }

}
