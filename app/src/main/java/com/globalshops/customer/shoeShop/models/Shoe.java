package com.globalshops.customer.shoeShop.models;

import java.util.HashMap;
import java.util.List;

public class Shoe {
    private String shoePriceTag;
    private String name;
    private String shortDesc;
    private String gender;
    private String productId;
    private List<String> shoeSizesList;
    private List<String> shoeColorsList;
    private List<String> imageUrls;
    private List<HashMap<String, String>> shoeSizeQuantityList;
    private List<HashMap<String, String>> shoeColorQuantityList;
    private List<String> imageNames;
    private String imageUrlAt0;
    private String orderNumber;
    private String orderStatus;

    private String shoeSize;
    private String ShoeColor;
    private String quantity;
    private String total;
    private String shopId;

    public Shoe() {
    }

    public Shoe(String shoePriceTag, String name, String shortDesc, String gender, String productId, List<String> shoeSizesList, List<String> shoeColorsList, List<String> imageUrls, List<HashMap<String, String>> shoeSizeQuantityList, List<HashMap<String, String>> shoeColorQuantityList, List<String> imageNames, String orderStatus) {
        this.shoePriceTag = shoePriceTag;
        this.name = name;
        this.shortDesc = shortDesc;
        this.gender = gender;
        this.productId = productId;
        this.shoeSizesList = shoeSizesList;
        this.shoeColorsList = shoeColorsList;
        this.imageUrls = imageUrls;
        this.shoeSizeQuantityList = shoeSizeQuantityList;
        this.shoeColorQuantityList = shoeColorQuantityList;
        this.imageNames = imageNames;
        this.orderStatus = orderStatus;
    }

    public Shoe(String shoePriceTag, String name, String productId, String shoeSize, String shoeColor, String quantity, String total, String imageUrlAt0, String orderNumber, String shopId) {
        this.shoePriceTag = shoePriceTag;
        this.name = name;
        this.productId = productId;
        this.shoeSize = shoeSize;
        ShoeColor = shoeColor;
        this.quantity = quantity;
        this.total = total;
        this.imageUrlAt0 = imageUrlAt0;
        this.orderNumber = orderNumber;
        this.shopId = shopId;

    }


    public String getShoePriceTag() {
        return shoePriceTag;
    }

    public void setShoePriceTag(String shoePriceTag) {
        this.shoePriceTag = shoePriceTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getShoeSizesList() {
        return shoeSizesList;
    }

    public void setShoeSizesList(List<String> shoeSizesList) {
        this.shoeSizesList = shoeSizesList;
    }

    public List<String> getShoeColorsList() {
        return shoeColorsList;
    }

    public void setShoeColorsList(List<String> shoeColorsList) {
        this.shoeColorsList = shoeColorsList;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<HashMap<String, String>> getShoeSizeQuantityList() {
        return shoeSizeQuantityList;
    }

    public void setShoeSizeQuantityList(List<HashMap<String, String>> shoeSizeQuantityList) {
        this.shoeSizeQuantityList = shoeSizeQuantityList;
    }

    public List<HashMap<String, String>> getShoeColorQuantityList() {
        return shoeColorQuantityList;
    }

    public void setShoeColorQuantityList(List<HashMap<String, String>> shoeColorQuantityList) {
        this.shoeColorQuantityList = shoeColorQuantityList;
    }

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getShoeColor() {
        return ShoeColor;
    }

    public void setShoeColor(String shoeColor) {
        ShoeColor = shoeColor;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImageUrlAt0() {
        return imageUrlAt0;
    }

    public void setImageUrlAt0(String imageUrlAt0) {
        this.imageUrlAt0 = imageUrlAt0;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
