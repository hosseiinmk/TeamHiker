package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class GroupMemberTool {
    private String response;
    @SerializedName("group_unique_id")
    private String groupUniqueId;
    @SerializedName("tool_name")
    private String toolName;
    @SerializedName("tool_number")
    private String toolNumber;
    @SerializedName("tool_type")
    private String toolType;

    public GroupMemberTool() {
    }

    public String getResponse() {
        return response;
    }

    public String getGroupUniqueId() {
        return groupUniqueId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolNumber() {
        return toolNumber;
    }

    public void setToolNumber(String toolNumber) {
        this.toolNumber = toolNumber;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }
}
