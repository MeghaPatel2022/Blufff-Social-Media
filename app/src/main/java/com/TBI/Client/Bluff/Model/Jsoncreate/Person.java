package com.TBI.Client.Bluff.Model.Jsoncreate;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Person implements Serializable {

    private final static long serialVersionUID = -5477604484015204325L;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Person(Integer position, List<Datum> data) {
        this.position = position;
        this.data = data;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "people{" +
                "position:" + position +
                ", data:" + data +
                '}';
    }
}
