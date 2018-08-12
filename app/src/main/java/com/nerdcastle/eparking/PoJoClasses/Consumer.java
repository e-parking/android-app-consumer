package com.nerdcastle.eparking.PoJoClasses;

public class Consumer {
    private String mComsumerID;
    private String mName;
    private String mPhoto;
    private String mAddress;
    private String mEmail;
    private String mPhone;
    private String mPassword;
    private String mNationalID;
    /*private String mVehicaleType;
    private String mVehicaleRegNo;
    private String mVehicaleOnwerName;
    private String mVehicaleOwnerID;
    private String mVehicalePaper;*/
    private String mCreatedDate;
    private String mLatitude;
    private String mLongitude;


    public Consumer() { }

    public Consumer(String mComsumerID, String mName, String mPhoto, String mAddress, String mEmail, String mPhone, String mPassword, String mNationalID, String mCreatedDate, String mLatitude, String mLongitude) {
        this.mComsumerID = mComsumerID;
        this.mName = mName;
        this.mPhoto = mPhoto;
        this.mAddress = mAddress;
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mNationalID = mNationalID;
        this.mCreatedDate = mCreatedDate;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }


    public String getmComsumerID() {
        return mComsumerID;
    }

    public void setmComsumerID(String mComsumerID) {
        this.mComsumerID = mComsumerID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
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

    @Override
    public String toString() {
        return "Consumer{" +
                "mComsumerID='" + mComsumerID + '\'' +
                ", mName='" + mName + '\'' +
                ", mPhoto='" + mPhoto + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mNationalID='" + mNationalID + '\'' +
                ", mCreatedDate='" + mCreatedDate + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                '}';
    }
}
