package com.TBI.Client.Bluff.Model.Get_bannerdetail;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Photo implements Serializable {

    private final static long serialVersionUID = 3582877423544370032L;
    @SerializedName("banner_img")
    @Expose
    private String bannerImg;

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bannerImg", bannerImg).toString();
    }

}
