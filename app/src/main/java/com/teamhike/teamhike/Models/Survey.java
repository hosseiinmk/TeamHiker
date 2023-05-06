package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Survey {
    private String response;
    private String owner;
    @SerializedName("favorite_attraction")
    private String favoriteAttraction;
    @SerializedName("favorite_natural_attraction")
    private String favoriteNaturalAttraction;
    @SerializedName("travel_with")
    private String travelWith;
    @SerializedName("travel_type")
    private String travelType;

    public String getResponse() {
        return response;
    }

    public String getOwner() {
        return owner;
    }

    public String getFavoriteAttraction() {
        return favoriteAttraction;
    }

    public String getFavoriteNaturalAttraction() {
        return favoriteNaturalAttraction;
    }

    public String getTravelWith() {
        return travelWith;
    }

    public String getTravelType() {
        return travelType;
    }
}
