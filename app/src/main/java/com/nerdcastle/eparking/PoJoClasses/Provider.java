package com.nerdcastle.eparking.PoJoClasses;

import com.nerdcastle.eparking.OtherClasses.ParkPlaceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Provider {


    private String mProviderID;
    private String mName;
    private String mProfilePhoto;
    private String mHouseHoldingPhoto1;
    private String mHouseHoldingPhoto2;
    private String mHouseHoldingPhoto3;
    private String mAddress;
    private String mEmail;
    private String mPhone;
    private String mPassword;
    private String mNationalID;
    private String mCreatedDate;
    private String mLatitude;
    private String mLongitude;
    private Map<String, ParkPlace> ParkPlaceList;
    //private List<Request> Request = new ArrayList<>();
    //private List<TransactionHistory> TransactionHistory = new ArrayList<>();




    public Provider() {
    }

    public Provider(String mProviderID, String mName, String mProfilePhoto, String mHouseHoldingPhoto1, String mHouseHoldingPhoto2, String mHouseHoldingPhoto3, String mAddress, String mEmail, String mPhone, String mPassword, String mNationalID, String mCreatedDate, String mLatitude, String mLongitude) {
        this.mProviderID = mProviderID;
        this.mName = mName;
        this.mProfilePhoto = mProfilePhoto;
        this.mHouseHoldingPhoto1 = mHouseHoldingPhoto1;
        this.mHouseHoldingPhoto2 = mHouseHoldingPhoto2;
        this.mHouseHoldingPhoto3 = mHouseHoldingPhoto3;
        this.mAddress = mAddress;
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mNationalID = mNationalID;
        this.mCreatedDate = mCreatedDate;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }


    public Map<String, ParkPlace> getParkPlaceList() {
        return ParkPlaceList;
    }

    public void setParkPlaceList(Map<String, ParkPlace> parkPlaceList) {
        ParkPlaceList = parkPlaceList;
    }

    public String getmProviderID() {
        return mProviderID;
    }

    public void setmProviderID(String mProviderID) {
        this.mProviderID = mProviderID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmProfilePhoto() {
        return mProfilePhoto;
    }

    public void setmProfilePhoto(String mProfilePhoto) {
        this.mProfilePhoto = mProfilePhoto;
    }

    public String getmHouseHoldingPhoto1() {
        return mHouseHoldingPhoto1;
    }

    public void setmHouseHoldingPhoto1(String mHouseHoldingPhoto1) {
        this.mHouseHoldingPhoto1 = mHouseHoldingPhoto1;
    }

    public String getmHouseHoldingPhoto2() {
        return mHouseHoldingPhoto2;
    }

    public void setmHouseHoldingPhoto2(String mHouseHoldingPhoto2) {
        this.mHouseHoldingPhoto2 = mHouseHoldingPhoto2;
    }

    public String getmHouseHoldingPhoto3() {
        return mHouseHoldingPhoto3;
    }

    public void setmHouseHoldingPhoto3(String mHouseHoldingPhoto3) {
        this.mHouseHoldingPhoto3 = mHouseHoldingPhoto3;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmNationalID() {
        return mNationalID;
    }

    public void setmNationalID(String mNationalID) {
        this.mNationalID = mNationalID;
    }

    public String getmCreatedDate() {
        return mCreatedDate;
    }

    public void setmCreatedDate(String mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
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


}
