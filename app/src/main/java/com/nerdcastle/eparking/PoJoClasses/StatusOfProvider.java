package com.nerdcastle.eparking.PoJoClasses;

public class StatusOfProvider {

    private String mProcessID;
    private String mConsumerID;
    private String mProviderID;
    private String mParkPlaceID;
    private String mRequestID;
    private String mConsumerName;
    private String mConsumerPhone;
    private String mConsumerVehicleNumber;
    private String mConsumerAddress;
    private String mConsumerLatitude;
    private String mConsumerLongitude;

    public StatusOfProvider() {
    }

    public StatusOfProvider(String mProcessID, String mConsumerID, String mProviderID, String mParkPlaceID, String mRequestID, String mConsumerName, String mConsumerPhone, String mConsumerVehicleNumber, String mConsumerAddress, String mConsumerLatitude, String mConsumerLongitude) {
        this.mProcessID = mProcessID;
        this.mConsumerID = mConsumerID;
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mRequestID = mRequestID;
        this.mConsumerName = mConsumerName;
        this.mConsumerPhone = mConsumerPhone;
        this.mConsumerVehicleNumber = mConsumerVehicleNumber;
        this.mConsumerAddress = mConsumerAddress;
        this.mConsumerLatitude = mConsumerLatitude;
        this.mConsumerLongitude = mConsumerLongitude;
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

    public String getmConsumerName() {
        return mConsumerName;
    }

    public void setmConsumerName(String mConsumerName) {
        this.mConsumerName = mConsumerName;
    }

    public String getmConsumerPhone() {
        return mConsumerPhone;
    }

    public void setmConsumerPhone(String mConsumerPhone) {
        this.mConsumerPhone = mConsumerPhone;
    }

    public String getmConsumerVehicleNumber() {
        return mConsumerVehicleNumber;
    }

    public void setmConsumerVehicleNumber(String mConsumerVehicleNumber) {
        this.mConsumerVehicleNumber = mConsumerVehicleNumber;
    }

    public String getmConsumerAddress() {
        return mConsumerAddress;
    }

    public void setmConsumerAddress(String mConsumerAddress) {
        this.mConsumerAddress = mConsumerAddress;
    }

    public String getmConsumerLatitude() {
        return mConsumerLatitude;
    }

    public void setmConsumerLatitude(String mConsumerLatitude) {
        this.mConsumerLatitude = mConsumerLatitude;
    }

    public String getmConsumerLongitude() {
        return mConsumerLongitude;
    }

    public void setmConsumerLongitude(String mConsumerLongitude) {
        this.mConsumerLongitude = mConsumerLongitude;
    }
}
