package com.nerdcastle.eparking.PoJoClasses;

/**
 * Created by Nipon on 9/19/2018.
 */

public class Schedule {

    private String mDay;
    private long mFromTime;
    private long mToTime;
    private String mIsForRent;

    public Schedule() {
    }

    public Schedule(String mDay, long mFromTime, long mToTime, String mIsForRent) {
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

    public long getmFromTime() {
        return mFromTime;
    }

    public void setmFromTime(long mFromTime) {
        this.mFromTime = mFromTime;
    }

    public long getmToTime() {
        return mToTime;
    }

    public void setmToTime(long mToTime) {
        this.mToTime = mToTime;
    }

    public String getmIsForRent() {
        return mIsForRent;
    }

    public void setmIsForRent(String mIsForRent) {
        this.mIsForRent = mIsForRent;
    }
}
