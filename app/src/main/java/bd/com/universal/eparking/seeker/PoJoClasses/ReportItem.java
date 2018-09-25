package bd.com.universal.eparking.seeker.PoJoClasses;

public class ReportItem {

    public String mReportID;
    public String mUserID;
    public String mUserName;
    public String mReportedMessage;

    public ReportItem() {
    }

    public ReportItem(String mReportID, String mUserID, String mUserName, String mReportedMessage) {
        this.mReportID = mReportID;
        this.mUserID = mUserID;
        this.mUserName = mUserName;
        this.mReportedMessage = mReportedMessage;
    }

    public String getmReportID() {
        return mReportID;
    }

    public void setmReportID(String mReportID) {
        this.mReportID = mReportID;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmReportedMessage() {
        return mReportedMessage;
    }

    public void setmReportedMessage(String mReportedMessage) {
        this.mReportedMessage = mReportedMessage;
    }


}
