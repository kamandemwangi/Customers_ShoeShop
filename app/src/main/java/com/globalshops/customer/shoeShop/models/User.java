package com.globalshops.customer.shoeShop.models;

public class User {
    private String fullNames;
    private String phoneNumber;
    private String email;
    private String userId;

    public User() {
    }

    public User(String fullNames, String phoneNumber, String email) {
        this.fullNames = fullNames;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
