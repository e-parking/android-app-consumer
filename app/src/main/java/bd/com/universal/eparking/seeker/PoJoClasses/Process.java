package bd.com.universal.eparking.seeker.PoJoClasses;

public class Process {
    private String mProcessID;
    private String isStarted;
    private String mStartedTime;
    private String mEndTime;
    private String mDuration;
    private String mAmount;

    private String mProviderID;
    private String mParkPlaceID;
    private String mRequestID;
    private String mConsumerID;
    private String mVehicleNumber;
    private String mVehicleType;

    public Process() {

    }

    public Process(String mProcessID, String isStarted, String mStartedTime, String mEndTime, String mDuration, String mAmount, String mProviderID, String mParkPlaceID, String mRequestID, String mConsumerID, String mVehicleNumber, String mVehicleType) {
        this.mProcessID = mProcessID;
        this.isStarted = isStarted;
        this.mStartedTime = mStartedTime;
        this.mEndTime = mEndTime;
        this.mDuration = mDuration;
        this.mAmount = mAmount;
        this.mProviderID = mProviderID;
        this.mParkPlaceID = mParkPlaceID;
        this.mRequestID = mRequestID;
        this.mConsumerID = mConsumerID;
        this.mVehicleNumber = mVehicleNumber;
        this.mVehicleType = mVehicleType;
    }

    public String getmProcessID() {
        return mProcessID;
    }

    public void setmProcessID(String mProcessID) {
        this.mProcessID = mProcessID;
    }

    public String getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(String isStarted) {
        this.isStarted = isStarted;
    }

    public String getmStartedTime() {
        return mStartedTime;
    }

    public void setmStartedTime(String mStartedTime) {
        this.mStartedTime = mStartedTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
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

    public String getmConsumerID() {
        return mConsumerID;
    }

    public void setmConsumerID(String mConsumerID) {
        this.mConsumerID = mConsumerID;
    }

    public String getmVehicleNumber() {
        return mVehicleNumber;
    }

    public void setmVehicleNumber(String mVehicleNumber) {
        this.mVehicleNumber = mVehicleNumber;
    }

    public String getmVehicleType() {
        return mVehicleType;
    }

    public void setmVehicleType(String mVehicleType) {
        this.mVehicleType = mVehicleType;
    }
}
