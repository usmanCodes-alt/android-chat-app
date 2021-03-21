package com.example.chatapp.models;

public class User {
    private String profilePicture;
    private String email;
    private String username;
    private String password;
    private String userId;
    private String lastMessage;

    public User() {
    }

    // Sign up constructor
    // we will use this constructor to store user in firebase
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String profilePicture, String email, String username, String password, String userId, String lastMessage) {
        this.profilePicture = profilePicture;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserId() {
        return this.userId;
    }
}
