package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Place {
    private String response;
    @SerializedName("place_unique_id")
    private String placeUniqueId;
    private String province;
    @SerializedName("attraction_place_number")
    private String attractionPlaceNumber;
    @SerializedName("enthusiasts_number")
    private String enthusiastsNumber;
    private String image;

    public Place() {
    }

    public String getResponse() {
        return response;
    }

    public String getPlaceUniqueId() {
        return placeUniqueId;
    }

    public String getProvince() {
        return province;
    }

    public String getAttractionPlaceNumber() {
        return attractionPlaceNumber;
    }

    public String getEnthusiastsNumber() {
        return enthusiastsNumber;
    }

    public String getImage() {
        return image;
    }
}
