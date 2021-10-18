package com.TBI.Client.Bluff.Model.NearBy;

import java.io.Serializable;
import java.util.List;

import com.TBI.Client.Bluff.Model.GlobalSearch.Persons;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetNearby implements Serializable {

    private final static long serialVersionUID = 7749681408234332253L;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Persons> data = null;

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

    public List<Persons> getData() {
        return data;
    }

    public void setData(List<Persons> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("data", data).toString();
    }

}
