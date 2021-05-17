package com.brzhnkv.liketime.user;


public class User {
    private String token;
    private String password;
    private String profilePicUrl;

    public User() {}

    public User(String token, String password, String profilePicUrl) {
        this.token = token;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
