package com.TBI.Client.Bluff.Model.GetToken;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = 1721184141148902367L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gif_images")
    @Expose
    private List<GifImage> gifImages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<GifImage> getGifImages() {
        return gifImages;
    }

    public void setGifImages(List<GifImage> gifImages) {
        this.gifImages = gifImages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("image", image).append("gifImages", gifImages).toString();
    }

}
