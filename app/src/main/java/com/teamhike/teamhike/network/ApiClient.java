package com.teamhike.teamhike.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // http://172.17.100.2/ for nox
    // http://10.0.2.2/ for other devices
    public static final String TEAM_HIKER_URL = "https://www.api.teamhiker.ir/";
    public static final String MAP_URL = "https://api.neshan.org/v1/";

    private static Retrofit mapRetrofit;
    private static Retrofit teamHikerRetrofit;

    public ApiClient() {}

    public static Retrofit getTeamHikerRetrofit() {
        if (teamHikerRetrofit == null) {
            teamHikerRetrofit = new Retrofit.Builder()
                    .baseUrl(TEAM_HIKER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return teamHikerRetrofit;
    }

    public static Retrofit getMapRetrofit() {
        if (mapRetrofit == null) {
            mapRetrofit = new Retrofit.Builder()
                    .baseUrl(MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mapRetrofit;
    }
}
