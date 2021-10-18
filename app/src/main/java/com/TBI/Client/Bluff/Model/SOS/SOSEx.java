package com.TBI.Client.Bluff.Model.SOS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SOSEx {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<SOSDetail> data = new ArrayList<>();
    @SerializedName("list")
    @Expose
    private List<SOSList> list = new ArrayList<>();

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

    public List<SOSDetail> getData() {
        return data;
    }

    public void setData(List<SOSDetail> data) {
        this.data = data;
    }

    public List<SOSList> getList() {
        return list;
    }

    public void setList(java.util.List<SOSList> list) {
        this.list = list;
    }

}
