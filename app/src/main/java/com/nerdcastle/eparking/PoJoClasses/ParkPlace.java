package com.nerdcastle.eparking.PoJoClasses;

public class ParkPlace {

    private String mProviderID;  //Auto
    private String mParkPlaceID;  //Auto
    private String mParkPlaceTitle;  //Auto
    private String mParkingType;  //Radio -----------
    private String mParkingStatus; //Auto
    private String mParkedVehicle;   //Auto --------------
    private String mParkingChargePerHour; //Set Amount ---------------

    /*
    private String mParkingIsApproved = "false";
    private String mLatitude;
    private String mLongitude;
    */




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


    @Override
    public String toString() {
        return "ParkPlace{" +
                "mProviderID='" + mProviderID + '\'' +
                ", mParkPlaceID='" + mParkPlaceID + '\'' +
                ", mParkPlaceTitle='" + mParkPlaceTitle + '\'' +
                ", mParkingType='" + mParkingType + '\'' +
                ", mParkingStatus='" + mParkingStatus + '\'' +
                ", mParkedVehicle='" + mParkedVehicle + '\'' +
                ", mParkingChargePerHour='" + mParkingChargePerHour + '\'' +
                '}';
    }
}
