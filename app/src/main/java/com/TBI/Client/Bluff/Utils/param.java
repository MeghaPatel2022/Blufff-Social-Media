package com.TBI.Client.Bluff.Utils;

import java.util.ArrayList;

public class param {

    String name;
    String value;
    ArrayList<String> value1;
    String token;

    public param(String name, String value) {
        this.value = value;
        this.name = name;
    }

    public param(String name, ArrayList<String> value1) {
        this.value1 = value1;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<String> getValue1() {
        return value1;
    }

    public void setValue1(ArrayList<String> value) {
        this.value1 = value1;
    }

    @Override
    public String toString() {
        return "param{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

}
