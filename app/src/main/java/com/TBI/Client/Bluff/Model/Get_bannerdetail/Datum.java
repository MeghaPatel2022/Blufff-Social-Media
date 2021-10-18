package com.TBI.Client.Bluff.Model.Get_bannerdetail;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = -7903964520001499546L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_mobile_code")
    @Expose
    private String businessMobileCode;
    @SerializedName("business_number")
    @Expose
    private String businessNumber;
    @SerializedName("business_email")
    @Expose
    private String businessEmail;
    @SerializedName("business_location")
    @Expose
    private String businessLocation;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("business_sub_topics")
    @Expose
    private String businessSubTopics;
    @SerializedName("business_type")
    @Expose
    private String business_type;

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessMobileCode() {
        return businessMobileCode;
    }

    public void setBusinessMobileCode(String businessMobileCode) {
        this.businessMobileCode = businessMobileCode;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBusinessSubTopics() {
        return businessSubTopics;
    }

    public void setBusinessSubTopics(String businessSubTopics) {
        this.businessSubTopics = businessSubTopics;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("title", title).append("banner", banner).append("photos", photos).append("businessName", businessName).append("businessMobileCode", businessMobileCode).append("businessNumber", businessNumber).append("businessEmail", businessEmail).append("businessLocation", businessLocation).append("lat", lat).append("lang", lang).append("businessSubTopics", businessSubTopics).toString();
    }

}
