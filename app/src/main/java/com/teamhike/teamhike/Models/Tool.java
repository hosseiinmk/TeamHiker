package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Tool {
    private String response;
    @SerializedName("tool_name")
    private String toolName;
    @SerializedName("tool_number")
    private String toolNumber;

    public String getResponse() {
        return response;
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
}
