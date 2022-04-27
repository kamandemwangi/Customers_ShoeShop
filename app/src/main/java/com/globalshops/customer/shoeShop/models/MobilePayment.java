package com.globalshops.customer.shoeShop.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobilePayment {

    private int BusinessShortCode;
    private String Password;
    private String Timestamp;
    private String TransactionType;
    private int Amount;
    private Long PartyA;
    private int PartyB;
    private Long PhoneNumber;
    private String CallBackURL;
    private String AccountReference;
    private String TransactionDesc;

    private String userId;


    public MobilePayment() {
    }

    public MobilePayment(int businessShortCode, String password, String timestamp, String transactionType, int amount, Long partyA, int partyB, Long phoneNumber, String callBackURL, String accountReference, String transactionDesc) {
        BusinessShortCode = businessShortCode;
        Password = password;
        Timestamp = timestamp;
        TransactionType = transactionType;
        Amount = amount;
        PartyA = partyA;
        PartyB = partyB;
        PhoneNumber = phoneNumber;
        CallBackURL = callBackURL;
        AccountReference = accountReference;
        TransactionDesc = transactionDesc;
    }

    public MobilePayment(String userId) {
        this.userId = userId;
    }

    public int getBusinessShortCode() {
        return BusinessShortCode;
    }

    public void setBusinessShortCode(int businessShortCode) {
        BusinessShortCode = businessShortCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public Long getPartyA() {
        return PartyA;
    }

    public void setPartyA(Long partyA) {
        this.PartyA = partyA;
    }

    public int getPartyB() {
        return PartyB;
    }

    public void setPartyB(int partyB) {
        this.PartyB = partyB;
    }

    public Long getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCallBackURL() {
        return CallBackURL;
    }

    public void setCallBackURL(String callBackURL) {
        this.CallBackURL = callBackURL;
    }

    public String getAccountReference() {
        return AccountReference;
    }

    public void setAccountReference(String accountReference) {
        AccountReference = accountReference;
    }

    public String getTransactionDesc() {
        return TransactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        TransactionDesc = transactionDesc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
