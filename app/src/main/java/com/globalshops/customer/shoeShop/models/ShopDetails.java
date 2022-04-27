package com.globalshops.customer.shoeShop.models;

public class ShopDetails {
    private String profileImageUrl;
    private String name;
    private String street;
    private String shopId;
    private String businessBuilding;
    private String shopNumber;

    public ShopDetails() {
    }

    public ShopDetails(String shopImageProfile, String name, String street, String shopId) {
        this.profileImageUrl = shopImageProfile;
        this.name = name;
        this.street = street;
        this.shopId = shopId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBusinessBuilding() {
        return businessBuilding;
    }

    public void setBusinessBuilding(String businessBuilding) {
        this.businessBuilding = businessBuilding;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }
}
