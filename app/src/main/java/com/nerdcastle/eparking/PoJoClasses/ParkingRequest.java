package com.nerdcastle.eparking.PoJoClasses;

public class ParkingRequest {

    private String mConsumerID;
    private String mProviderID;
    private String mParkPlaceID;
    private String mParkPlaceTitle;
    private String mRequestID;
    private String mConsumerName;
    private String mConsumerPhone;
    private String mConsumerVehicleNumber;

    private String mProviderName;
    private String mProviderPhone;
    private String mParkPlaceAddress;
    private String mParkPlaceLatitude;
    private String mParkPlaceLongitude;
    private String mStatus;
    private String mParkPlacePhotoUrl;

    public ParkingRequest() {
    }

    public ParkingRequest(String mConsumerID, String mProviderID, String mParkPlaceID, String mParkPlaceTitle, String mRequestID, String mConsumerName, String mConsumerPhone, String mConsumerVehicleNumber, String mProviderName, String mProviderPhone, String mParkPlaceAddress, String mParkPlaceLatitude, String mParkPlaceLongitude, String mStatus,String mParkPlacePhotoUrl) {
        this.mConsumerID = mConsumerID;
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mParkPlaceTitle = mParkPlaceTitle;
        this.mRequestID = mRequestID;
        this.mConsumerName = mConsumerName;
        this.mConsumerPhone = mConsumerPhone;
        this.mConsumerVehicleNumber = mConsumerVehicleNumber;
        this.mProviderName = mProviderName;
        this.mProviderPhone = mProviderPhone;
        this.mParkPlaceAddress = mParkPlaceAddress;
        this.mParkPlaceLatitude = mParkPlaceLatitude;
        this.mParkPlaceLongitude = mParkPlaceLongitude;
        this.mStatus = mStatus;
        this.mParkPlacePhotoUrl=mParkPlacePhotoUrl;
    }

    public String getmParkPlacePhotoUrl() {
        return mParkPlacePhotoUrl;
    }

    public void setmParkPlacePhotoUrl(String mParkPlacePhotoUrl) {
        this.mParkPlacePhotoUrl = mParkPlacePhotoUrl;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmParkPlaceTitle() {
        return mParkPlaceTitle;
    }

    public void setmParkPlaceTitle(String mParkPlaceTitle) {
        this.mParkPlaceTitle = mParkPlaceTitle;
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

    public String getmParkPlaceAddress() {
        return mParkPlaceAddress;
    }

    public void setmParkPlaceAddress(String mParkPlaceAddress) {
        this.mParkPlaceAddress = mParkPlaceAddress;
    }

    public String getmParkPlaceLatitude() {
        return mParkPlaceLatitude;
    }

    public void setmParkPlaceLatitude(String mParkPlaceLatitude) {
        this.mParkPlaceLatitude = mParkPlaceLatitude;
    }

    public String getmParkPlaceLongitude() {
        return mParkPlaceLongitude;
    }

    public void setmParkPlaceLongitude(String mParkPlaceLongitude) {
        this.mParkPlaceLongitude = mParkPlaceLongitude;
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

}
