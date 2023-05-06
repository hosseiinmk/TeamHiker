package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Experience {
    private String response;
    @SerializedName("user_unique_id")
    private String userUniqueId;
    @SerializedName("experience_unique_id")
    private String experienceUniqueId;
    private String location;
    private String description;
    private String image;
    @SerializedName("good_notes")
    private String goodNotes;
    @SerializedName("bad_notes")
    private String badNotes;
    private String views;
    private String likes;

    public String getResponse() {
        return response;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public String getExperienceUniqueId() {
        return experienceUniqueId;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getGoodNotes() {
        return goodNotes;
    }

    public String getBadNotes() {
        return badNotes;
    }

    public String getViews() {
        return views;
    }

    public String getLikes() {
        return likes;
    }
}
