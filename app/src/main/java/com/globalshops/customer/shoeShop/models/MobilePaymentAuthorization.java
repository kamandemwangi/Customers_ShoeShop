package com.globalshops.customer.shoeShop.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobilePaymentAuthorization {

    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("expires_in")
    @Expose
    private String expiry_in;

    public MobilePaymentAuthorization() {
    }

    public MobilePaymentAuthorization(String access_token, String expiry_in) {
        this.access_token = access_token;
        this.expiry_in = expiry_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpiry_in() {
        return expiry_in;
    }

    public void setExpiry_in(String expiry_in) {
        this.expiry_in = expiry_in;
    }
}
