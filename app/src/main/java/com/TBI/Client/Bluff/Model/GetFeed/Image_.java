package com.TBI.Client.Bluff.Model.GetFeed;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Image_ implements Serializable {

    private final static long serialVersionUID = -3375350111069164351L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("seen")
    @Expose
    private Integer seen;
    @SerializedName("filetype")
    @Expose
    private String filetype;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("story_date")
    @Expose
    private String storyDate;

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(String storyDate) {
        this.storyDate = storyDate;
    }

    @Override
    public String toString() {
        return "Image_{" +
                "id=" + id +
                ", userId=" + userId +
                ", seen=" + seen +
                ", filetype='" + filetype + '\'' +
                ", filename='" + filename + '\'' +
                ", storyDate='" + storyDate + '\'' +
                '}';
    }
}
