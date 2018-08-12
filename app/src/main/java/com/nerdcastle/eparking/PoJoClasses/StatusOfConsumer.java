package com.nerdcastle.eparking.PoJoClasses;

public class StatusOfConsumer {
    private String mProcessID;
    private String mConsumerID;
    private String mProviderID;
    private String mParkPlaceID;
    private String mRequestID;
    private String mProviderName;
    private String mProviderPhone;
    private String mProviderAddress;
    private String mProviderLatitude;
    private String mProviderLongitude;

    public StatusOfConsumer() {
    }

    public StatusOfConsumer(String mProcessID, String mConsumerID, String mProviderID, String mParkPlaceID, String mRequestID, String mProviderName, String mProviderPhone, String mProviderAddress, String mProviderLatitude, String mProviderLongitude) {
        this.mProcessID = mProcessID;
        this.mConsumerID = mConsumerID;
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mRequestID = mRequestID;
        this.mProviderName = mProviderName;
        this.mProviderPhone = mProviderPhone;
        this.mProviderAddress = mProviderAddress;
        this.mProviderLatitude = mProviderLatitude;
        this.mProviderLongitude = mProviderLongitude;
    }

    public String getmProcessID() {
        return mProcessID;
    }

    public void setmProcessID(String mProcessID) {
        this.mProcessID = mProcessID;
    }

    public String getmConsumerID() {
        return mConsumerID;
    }

    public void setmConsumerID(String mConsumerID) {
        this.mConsumerID = mConsumerID;
    }

    public String getmProviderID() {
        return mProviderID;
    }

    public void setmProviderID(String mProviderID) {
        this.mProviderID = mProviderID;
    }

    public String getmParkPlaceID() {
        return mParkPlaceID;
    }

    public void setmParkPlaceID(String mParkPlaceID) {
        this.mParkPlaceID = mParkPlaceID;
    }

    public String getmRequestID() {
        return mRequestID;
    }

    public void setmRequestID(String mRequestID) {
        this.mRequestID = mRequestID;
    }

    public String getmProviderName() {
        return mProviderName;
    }

    public void setmProviderName(String mProviderName) {
        this.mProviderName = mProviderName;
    }

    public String getmProviderPhone() {
        return mProviderPhone;
    }

    public void setmProviderPhone(String mProviderPhone) {
        this.mProviderPhone = mProviderPhone;
    }

    public String getmProviderAddress() {
        return mProviderAddress;
    }

    public void setmProviderAddress(String mProviderAddress) {
        this.mProviderAddress = mProviderAddress;
    }

    public String getmProviderLatitude() {
        return mProviderLatitude;
    }

    public void setmProviderLatitude(String mProviderLatitude) {
        this.mProviderLatitude = mProviderLatitude;
    }

    public String getmProviderLongitude() {
        return mProviderLongitude;
    }

    public void setmProviderLongitude(String mProviderLongitude) {
        this.mProviderLongitude = mProviderLongitude;
    }
}
