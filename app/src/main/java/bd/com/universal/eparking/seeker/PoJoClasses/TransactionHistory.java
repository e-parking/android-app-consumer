package bd.com.universal.eparking.seeker.PoJoClasses;

public class TransactionHistory {

    private String mTransactionID;
    private String mTransactionAmount;
    private String mTransactionCreated;
    private String mTransactionAddress;
    private String mStartedTime;
    private String mEndedTime;
    private String mDuration;
    //-----------------------------
    private String mConsumerID;
    private String mConsumerName;
    private String mVehicleNumber;
    private String mVehicleType;
    //------------------------------
    private String mProviderID;
    private String mProviderName;
    private String mLatitude;
    private String mLogitude;


    public TransactionHistory() {
    }

    public TransactionHistory(String mTransactionID, String mTransactionAmount, String mTransactionCreated, String mTransactionAddress, String mStartedTime, String mEndedTime, String mDuration, String mConsumerID, String mConsumerName, String mVehicleNumber, String mVehicleType, String mProviderID, String mProviderName, String mLatitude, String mLogitude) {
        this.mTransactionID = mTransactionID;
        this.mTransactionAmount = mTransactionAmount;
        this.mTransactionCreated = mTransactionCreated;
        this.mTransactionAddress = mTransactionAddress;
        this.mStartedTime = mStartedTime;
        this.mEndedTime = mEndedTime;
        this.mDuration = mDuration;
        this.mConsumerID = mConsumerID;
        this.mConsumerName = mConsumerName;
        this.mVehicleNumber = mVehicleNumber;
        this.mVehicleType = mVehicleType;
        this.mProviderID = mProviderID;
        this.mProviderName = mProviderName;
        this.mLatitude = mLatitude;
        this.mLogitude = mLogitude;
    }


    public String getmTransactionID() {
        return mTransactionID;
    }

    public void setmTransactionID(String mTransactionID) {
        this.mTransactionID = mTransactionID;
    }

    public String getmTransactionAmount() {
        return mTransactionAmount;
    }

    public void setmTransactionAmount(String mTransactionAmount) {
        this.mTransactionAmount = mTransactionAmount;
    }

    public String getmTransactionCreated() {
        return mTransactionCreated;
    }

    public void setmTransactionCreated(String mTransactionCreated) {
        this.mTransactionCreated = mTransactionCreated;
    }

    public String getmTransactionAddress() {
        return mTransactionAddress;
    }

    public void setmTransactionAddress(String mTransactionAddress) {
        this.mTransactionAddress = mTransactionAddress;
    }

    public String getmStartedTime() {
        return mStartedTime;
    }

    public void setmStartedTime(String mStartedTime) {
        this.mStartedTime = mStartedTime;
    }

    public String getmEndedTime() {
        return mEndedTime;
    }

    public void setmEndedTime(String mEndedTime) {
        this.mEndedTime = mEndedTime;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmConsumerID() {
        return mConsumerID;
    }

    public void setmConsumerID(String mConsumerID) {
        this.mConsumerID = mConsumerID;
    }

    public String getmConsumerName() {
        return mConsumerName;
    }

    public void setmConsumerName(String mConsumerName) {
        this.mConsumerName = mConsumerName;
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

    public String getmProviderID() {
        return mProviderID;
    }

    public void setmProviderID(String mProviderID) {
        this.mProviderID = mProviderID;
    }

    public String getmProviderName() {
        return mProviderName;
    }

    public void setmProviderName(String mProviderName) {
        this.mProviderName = mProviderName;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLogitude() {
        return mLogitude;
    }

    public void setmLogitude(String mLogitude) {
        this.mLogitude = mLogitude;
    }

}
