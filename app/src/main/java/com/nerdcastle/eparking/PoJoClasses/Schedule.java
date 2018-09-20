package com.nerdcastle.eparking.PoJoClasses;

/**
 * Created by Nipon on 9/19/2018.
 */

public class Schedule {

    private String mDay;
    private String mFromTime;
    private String mToTime;
    private String mIsForRent;

    public Schedule() {
    }


    public Schedule(String mDay, String mFromTime, String mToTime, String mIsForRent) {
        this.mDay = mDay;
        this.mFromTime = mFromTime;
        this.mToTime = mToTime;
        this.mIsForRent = mIsForRent;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public String getmFromTime() {
        return mFromTime;
    }

    public void setmFromTime(String mFromTime) {
        this.mFromTime = mFromTime;
    }

    public String getmToTime() {
        return mToTime;
    }

    public void setmToTime(String mToTime) {
        this.mToTime = mToTime;
    }

    public String getmIsForRent() {
        return mIsForRent;
    }

    public void setmIsForRent(String mIsForRent) {
        this.mIsForRent = mIsForRent;
    }
}
