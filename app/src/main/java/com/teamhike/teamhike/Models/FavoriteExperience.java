package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class FavoriteExperience {
    private String response;
    @SerializedName("experience_unique_id")
    private String experienceUniqueId;
    @SerializedName("user_unique_id")
    private String userUniqueId;

    public FavoriteExperience() {
    }

    public String getResponse() {
        return response;
    }

    public String getExperienceUniqueId() {
        return experienceUniqueId;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }
}
