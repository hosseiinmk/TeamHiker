package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("title")
    private String provinceName;

    public String getProvinceName() {
        return provinceName;
    }
}
