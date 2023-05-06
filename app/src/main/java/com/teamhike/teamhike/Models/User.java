package com.teamhike.teamhike.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    private String response;
    private String username;
    private String name;
    private String email;
    private String birthday;
    private String city;
    private String gender;
    private String experience;
    @SerializedName("about_me")
    private String aboutMe;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String image;
    private String logged;
    @SerializedName("friend_phone_number")
    private String friendPhoneNumber;
    @SerializedName("user_unique_id")
    private String userUniqueId;
    private String post;
    private Integer admin;

    public User() {}

    public String getResponse() {
        return response;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public String getLogged() {
        return logged;
    }

    public void setLogged(String logged) {
        this.logged = logged;
    }

    public String getFriendPhoneNumber() {
        return friendPhoneNumber;
    }

    public String getUniqueId() {
        return userUniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.userUniqueId = uniqueId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getAdmin() {
        return admin;
    }
}
