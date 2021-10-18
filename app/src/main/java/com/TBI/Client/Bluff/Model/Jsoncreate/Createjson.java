package com.TBI.Client.Bluff.Model.Jsoncreate;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Createjson implements Serializable {

    private final static long serialVersionUID = 8730819242216708527L;
    @SerializedName("people")
    @Expose
    private List<Person> people = null;

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "Createjson{" +
                "'people='" + people +
                '}';
    }
}
