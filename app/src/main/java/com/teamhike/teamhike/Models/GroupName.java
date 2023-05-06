package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class GroupName {
    private String response;
    @SerializedName("group_unique_id")
    private String groupUniqueId;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("group_image")
    private String groupImage;

    public String getResponse() {
        return response;
    }

    public String getGroupUniqueId() {
        return groupUniqueId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }
}
