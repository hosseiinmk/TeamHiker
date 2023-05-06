package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Group {
    private String response;
    @SerializedName("group_unique_id")
    private String groupUniqueId;
    @SerializedName("creator_unique_id")
    private String creatorUniqueId;
    @SerializedName("leader_unique_id")
    private String leaderUniqueId;
    @SerializedName("destination_province")
    private String destinationProvince;
    @SerializedName("map_longitude")
    private String mapLongitude;
    @SerializedName("map_latitude")
    private String mapLatitude;
    @SerializedName("travel_hardness_level")
    private String travelHardnessLevel;
    private Integer accepted;

    public String getResponse() {
        return response;
    }

    public String getGroupUniqueId() {
        return groupUniqueId;
    }

    public String getCreatorUniqueId() {
        return creatorUniqueId;
    }

    public String getLeaderUniqueId() {
        return leaderUniqueId;
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public String getMapLongitude() {
        return mapLongitude;
    }

    public String getMapLatitude() {
        return mapLatitude;
    }

    public String getTravelHardnessLevel() {
        return travelHardnessLevel;
    }

    public Integer getAccepted() {
        return accepted;
    }
}
