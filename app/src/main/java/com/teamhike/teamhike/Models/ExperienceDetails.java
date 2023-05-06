package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class ExperienceDetails {
    private String response;
    @SerializedName("user_unique_id")
    private String ownerUniqueId;
    @SerializedName("experience_unique_id")
    private String experienceUniqueId;
    @SerializedName("sender_unique_id")
    private String senderUniqueId;
    private String comment;

    public String getResponse() {
        return response;
    }

    public String getOwnerUniqueId() {
        return ownerUniqueId;
    }

    public String getExperienceUniqueId() {
        return experienceUniqueId;
    }

    public String getSenderUniqueId() {
        return senderUniqueId;
    }

    public String getComment() {
        return comment;
    }
}
