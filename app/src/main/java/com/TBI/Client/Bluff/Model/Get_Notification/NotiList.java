package com.TBI.Client.Bluff.Model.Get_Notification;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NotiList implements Serializable {

    private final static long serialVersionUID = 2019228616593909698L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("uimage")
    @Expose
    private String uimage;
    @SerializedName("pimage")
    @Expose
    private String pimage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("follow_by_you")
    @Expose
    private Integer followByYou;

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

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getFollowByYou() {
        return followByYou;
    }

    public void setFollowByYou(Integer followByYou) {
        this.followByYou = followByYou;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("type", type).append("to", to).append("from", from).append("postId", postId).append("commentId", commentId).append("uimage", uimage).append("pimage", pimage).append("description", description).append("duration", duration).append("followByYou", followByYou).toString();
    }

}
