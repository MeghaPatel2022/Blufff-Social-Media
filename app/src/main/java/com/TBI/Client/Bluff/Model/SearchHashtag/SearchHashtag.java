package com.TBI.Client.Bluff.Model.SearchHashtag;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchHashtag implements Serializable {

    private final static long serialVersionUID = -4986945037406680944L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("hastags")
    @Expose
    private List<Hastag> hastags = null;

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

    public List<Hastag> getHastags() {
        return hastags;
    }

    public void setHastags(List<Hastag> hastags) {
        this.hastags = hastags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("hastags", hastags).toString();
    }

}
