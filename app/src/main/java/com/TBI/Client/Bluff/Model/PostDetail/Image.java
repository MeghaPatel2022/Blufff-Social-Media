package com.TBI.Client.Bluff.Model.PostDetail;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Image implements Serializable {

    private final static long serialVersionUID = 1488821458544976842L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tag_people")
    @Expose
    private List<TagPerson> tagPeople = null;
    @SerializedName("tag_show")
    @Expose
    private boolean tagshow = false;

    public boolean isTagshow() {
        return tagshow;
    }

    public void setTagshow(boolean tagshow) {
        this.tagshow = tagshow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TagPerson> getTagPeople() {
        return tagPeople;
    }

    public void setTagPeople(List<TagPerson> tagPeople) {
        this.tagPeople = tagPeople;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("postId", postId).append("image", image).append("description", description).append("tagPeople", tagPeople).toString();
    }

}
