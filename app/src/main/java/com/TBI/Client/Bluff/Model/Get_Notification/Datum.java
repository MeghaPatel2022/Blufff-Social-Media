package com.TBI.Client.Bluff.Model.Get_Notification;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = -5481546283667288543L;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("list")
    @Expose
    private List<NotiList> list = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NotiList> getList() {
        return list;
    }

    public void setList(List<NotiList> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("list", list).toString();
    }

}
