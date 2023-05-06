package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Blog {
    private String response;
    @SerializedName("blog_unique_id")
    private String blogUniqueId;
    private String province;
    private String destination;
    private String image;
    private String description;
    @SerializedName("static_location_image")
    private String staticLocationImage;

    public Blog() {
    }

    public String getResponse() {
        return response;
    }

    public String getBlogUniqueId() {
        return blogUniqueId;
    }

    public String getProvince() {
        return province;
    }

    public String getDestination() {
        return destination;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getStaticLocationImage() {
        return staticLocationImage;
    }
}
