package com.nerdcastle.eparking.PoJoClasses;

public class SelfBook {

    private String date;
    private long startTime;
    private long endTime;
    private String selfBookId;

    public SelfBook(String selfBookId, String date, long startTime, long endTime) {
        this.selfBookId = selfBookId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SelfBook() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSelfBookId() {
        return selfBookId;
    }

    public void setSelfBookId(String selfBookId) {
        this.selfBookId = selfBookId;
    }
}
