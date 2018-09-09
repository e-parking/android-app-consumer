package com.nerdcastle.eparking.PoJoClasses;

public class ParkPlace {

    private String mProviderID;  //Auto
    private String mParkPlaceID;  //Auto
    private String mParkPlaceTitle;  //Auto
    private String mParkingType;  //Radio -----------
    private String mParkingStatus; //Auto
    private String mParkedVehicle;   //Auto --------------
    private String mParkingChargePerHour; //Set Amount ---------------
    private String mAddress; //Auto
    private String mLatitude; //Auto
    private String mLongitude; //Auto
    private String mParkingIsApproved; //Auto


    private String mHouseNumber; //Auto
    private String mRoadNumber; //Auto
    private String mCityName; //Auto
    private String mAreaName; //Auto

    private String mParkPlacePhotoUrl;//Auto

    // for checking parking is available or not
    private String mIsAvailable;


    public ParkPlace() {
    }

    public ParkPlace(String mProviderID, String mParkPlaceID, String mParkPlaceTitle, String mParkingType, String mParkingStatus, String mParkedVehicle, String mParkingChargePerHour) {
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mParkPlaceTitle = mParkPlaceTitle;
        this.mParkingType = mParkingType;
        this.mParkingStatus = mParkingStatus;
        this.mParkedVehicle = mParkedVehicle;
        this.mParkingChargePerHour = mParkingChargePerHour;
    }


    public ParkPlace(String mProviderID, String mParkPlaceID, String mParkPlaceTitle, String mParkingType, String mParkingStatus, String mParkedVehicle, String mParkingChargePerHour, String mAddress, String mLatitude, String mLongitude, String mParkingIsApproved, String mHouseNumber, String mRoadNumber, String mCityName, String mAreaName, String mParkPlacePhotoUrl, String mIsAvailable) {
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mParkPlaceTitle = mParkPlaceTitle;
        this.mParkingType = mParkingType;
        this.mParkingStatus = mParkingStatus;
        this.mParkedVehicle = mParkedVehicle;
        this.mParkingChargePerHour = mParkingChargePerHour;
        this.mAddress = mAddress;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mParkingIsApproved = mParkingIsApproved;
        this.mHouseNumber = mHouseNumber;
        this.mRoadNumber = mRoadNumber;
        this.mCityName = mCityName;
        this.mAreaName = mAreaName;
        this.mParkPlacePhotoUrl = mParkPlacePhotoUrl;
        this.mIsAvailable=mIsAvailable;
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

    public String getmParkPlaceTitle() {
        return mParkPlaceTitle;
    }

    public void setmParkPlaceTitle(String mParkPlaceTitle) {
        this.mParkPlaceTitle = mParkPlaceTitle;
    }

    public String getmParkingType() {
        return mParkingType;
    }

    public void setmParkingType(String mParkingType) {
        this.mParkingType = mParkingType;
    }

    public String getmParkingStatus() {
        return mParkingStatus;
    }

    public void setmParkingStatus(String mParkingStatus) {
        this.mParkingStatus = mParkingStatus;
    }

    public String getmParkedVehicle() {
        return mParkedVehicle;
    }

    public void setmParkedVehicle(String mParkedVehicle) {
        this.mParkedVehicle = mParkedVehicle;
    }

    public String getmParkingChargePerHour() {
        return mParkingChargePerHour;
    }

    public void setmParkingChargePerHour(String mParkingChargePerHour) {
        this.mParkingChargePerHour = mParkingChargePerHour;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmParkingIsApproved() {
        return mParkingIsApproved;
    }

    public void setmParkingIsApproved(String mParkingIsApproved) {
        this.mParkingIsApproved = mParkingIsApproved;
    }

    public String getmHouseNumber() {
        return mHouseNumber;
    }

    public void setmHouseNumber(String mHouseNumber) {
        this.mHouseNumber = mHouseNumber;
    }

    public String getmRoadNumber() {
        return mRoadNumber;
    }

    public void setmRoadNumber(String mRoadNumber) {
        this.mRoadNumber = mRoadNumber;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmAreaName() {
        return mAreaName;
    }

    public void setmAreaName(String mAreaName) {
        this.mAreaName = mAreaName;
    }

    public String getmParkPlacePhotoUrl() {
        return mParkPlacePhotoUrl;
    }

    public void setmParkPlacePhotoUrl(String mParkPlacePhotoUrl) {
        this.mParkPlacePhotoUrl = mParkPlacePhotoUrl;
    }

    public String getmIsAvailable() {
        return mIsAvailable;
    }

    public void setmIsAvailable(String mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
    }
}
