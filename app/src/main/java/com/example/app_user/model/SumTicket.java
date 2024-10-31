package com.example.app_user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SumTicket implements Parcelable  {
    private String userID;
    private String tongSoChuyenBay;
    private String tongSoLuongVeDat;

    protected SumTicket(Parcel in) {
        userID = in.readString();
        tongSoChuyenBay = in.readString();
        tongSoLuongVeDat = in.readString();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTongSoChuyenBay() {
        return tongSoChuyenBay;
    }

    public void setTongSoChuyenBay(String tongSoChuyenBay) {
        this.tongSoChuyenBay = tongSoChuyenBay;
    }

    public String getTongSoLuongVeDat() {
        return tongSoLuongVeDat;
    }

    public void setTongSoLuongVeDat(String tongSoLuongVeDat) {
        this.tongSoLuongVeDat = tongSoLuongVeDat;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(tongSoChuyenBay);
        dest.writeString(tongSoLuongVeDat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SumTicket> CREATOR = new Creator<SumTicket>() {
        @Override
        public SumTicket createFromParcel(Parcel in) {
            return new SumTicket(in);
        }

        @Override
        public SumTicket[] newArray(int size) {
            return new SumTicket[size];
        }
    };
}
