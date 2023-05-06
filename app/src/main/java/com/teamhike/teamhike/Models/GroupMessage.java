package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class GroupMessage {
    private String response;
    @SerializedName("group_unique_id")
    private String group_unique_id;
    @SerializedName("sender_unique_id")
    private String senderUniqueId;
    private String message;

    public GroupMessage() {
    }

    public String getResponse() {
        return response;
    }

    public String getGroup_unique_id() {
        return group_unique_id;
    }

    public String getSenderUniqueId() {
        return senderUniqueId;
    }

    public String getMessage() {
        return message;
    }
}
