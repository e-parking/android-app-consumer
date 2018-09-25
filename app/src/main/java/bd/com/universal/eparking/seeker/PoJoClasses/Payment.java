package bd.com.universal.eparking.seeker.PoJoClasses;

public class Payment {

    private String mTransactionDate, mPlaceType, mEnteringTime, mLeavingTime, mTransactionAmount, mAddress;


    public Payment(String mTransactionDate, String mPlaceType, String mEnteringTime, String mLeavingTime, String mTransactionAmount, String mAddress) {

        this.mTransactionDate = mTransactionDate;
        this.mPlaceType = mPlaceType;
        this.mEnteringTime = mEnteringTime;
        this.mLeavingTime = mLeavingTime;
        this.mTransactionAmount = mTransactionAmount;
        this.mAddress = mAddress;
    }

    public String getMTransactionDate() {
        return mTransactionDate;
    }
    public void setMTransactionDate(String mTransactionDate) {
        this.mTransactionDate = mTransactionDate;
    }

    public String getMPlaceType() {
        return mPlaceType;
    }
    public void setMPlaceType(String mPlaceType) {
        this.mPlaceType = mPlaceType;
    }

    public String getMEnteringTime() {
        return mEnteringTime;
    }
    public void setMEnteringTime(String mEnteringTime) {
        this.mEnteringTime = mEnteringTime;
    }

    public String getMLeavingTime() {
        return mLeavingTime;
    }
    public void setMLeavingTime(String mLeavingTime) {
        this.mLeavingTime = mLeavingTime;
    }

    public String getMTransactionAmount() {
        return mTransactionAmount;
    }
    public void setMTransactionAmount(String mTransactionAmount) {
        this.mTransactionAmount = mTransactionAmount;
    }

    public String getMAddress() {
        return mAddress;
    }
    public void setMAddress(String mAddress) {
        this.mAddress = mAddress;
    }

}