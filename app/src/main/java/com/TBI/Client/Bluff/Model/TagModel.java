package com.TBI.Client.Bluff.Model;

import com.harsh.instatag.InstaTag;

import java.util.Objects;

public class TagModel {

    InstaTag instaTag;
    int position;

    public TagModel(InstaTag instaTag, int position) {
        this.instaTag = instaTag;
        this.position = position;
    }


    public InstaTag getInstaTag() {
        return instaTag;
    }

    public void setInstaTag(InstaTag instaTag) {
        this.instaTag = instaTag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public String toString() {
        return "TagModel{" +
                "instaTag=" + instaTag +
                ", position=" + position +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        return (this.position == ((TagModel) obj).position);
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagModel tagModel = (TagModel) o;
        return position == tagModel.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instaTag, position);
    }*/
}
