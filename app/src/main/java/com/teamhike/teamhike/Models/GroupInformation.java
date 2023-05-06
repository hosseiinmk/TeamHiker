package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class GroupInformation {
    private String response;
    @SerializedName("group_unique_id")
    private String groupUniqueId;
    @SerializedName("leader_unique_id")
    private String leaderUniqueId;
    @SerializedName("minimum_member")
    private String minimumMember;
    @SerializedName("maximum_member")
    private String maximumMember;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("finish_date")
    private String finishDate;
    @SerializedName("start_place")
    private String startPlace;
    @SerializedName("finish_place")
    private String finishPlace;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("finish_time")
    private String finishTime;
    @SerializedName("target_place_1")
    private String targetPlace_1;
    @SerializedName("target_place_2")
    private String targetPlace_2;
    @SerializedName("target_place_3")
    private String targetPlace_3;
    @SerializedName("target_place_4")
    private String targetPlace_4;
    @SerializedName("target_place_5")
    private String targetPlace_5;
    @SerializedName("needed_on_way")
    private String needOnWay;
    private String meals;
    @SerializedName("more_notes")
    private String moreNotes;

    public String getResponse() {
        return response;
    }

    public String getGroupUniqueId() {
        return groupUniqueId;
    }

    public String getLeaderUniqueId() {
        return leaderUniqueId;
    }

    public String getMinimumMember() {
        return minimumMember;
    }

    public String getMaximumMember() {
        return maximumMember;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getFinishPlace() {
        return finishPlace;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getTargetPlace_1() {
        return targetPlace_1;
    }

    public String getTargetPlace_2() {
        return targetPlace_2;
    }

    public String getTargetPlace_3() {
        return targetPlace_3;
    }

    public String getTargetPlace_4() {
        return targetPlace_4;
    }

    public String getTargetPlace_5() {
        return targetPlace_5;
    }

    public String getNeedOnWay() {
        return needOnWay;
    }

    public String getMeals() {
        return meals;
    }

    public String getMoreNotes() {
        return moreNotes;
    }
}
