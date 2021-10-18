package com.TBI.Client.Bluff.Model.GetFeed;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.PostDetail.TagPerson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class Image implements Serializable {

    private final static long serialVersionUID = -4027017529021147419L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tag_people")
    @Expose
    private List<TagPerson> tagPeople = null;
    @SerializedName("tag_show")
    @Expose
    private boolean tagshow = false;

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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public boolean isTagshow() {
        return tagshow;
    }

    public void setTagshow(boolean tagshow) {
        this.tagshow = tagshow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("postId", postId).append("image", image).append("description", description).toString();
    }

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();

        try {
            json.put("id", id);
            json.put("postId", postId);
            json.put("image", image);
            json.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}
