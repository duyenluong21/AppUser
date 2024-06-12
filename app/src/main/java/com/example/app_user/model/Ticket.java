package com.example.app_user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket implements Parcelable {
    private String maVe;
    private String maCB;
    private String loaiVe;
    private String soLuong;
    private String soLuongCon;
    private String hangVe;

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getMaCB() {
        return maCB;
    }

    public void setMaCB(String maCB) {
        this.maCB = maCB;
    }

    public String getLoaiVe() {
        return loaiVe;
    }

    public void setLoaiVe(String loaiVe) {
        this.loaiVe = loaiVe;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getSoLuongCon() {
        return soLuongCon;
    }

    public void setSoLuongCon(String soLuongCon) {
        this.soLuongCon = soLuongCon;
    }

    public String getHangVe() {
        return hangVe;
    }

    public void setHangVe(String hangVe) {
        this.hangVe = hangVe;
    }
    protected Ticket(Parcel in) {
        maCB = in.readString();
        maVe = in.readString();
        loaiVe = in.readString();
        soLuong = in.readString();
        soLuongCon = in.readString();
        hangVe = in.readString();
    }
    public String toString() {
        return "Ticket{" +
                "maCB='" + maCB + '\'' +
                ", maVe='" + maVe + '\'' +
                ", loaiVe='" + loaiVe + '\'' +
                ", soLuong='" + soLuong + '\'' +
                ", soLuongCon='" + soLuongCon + '\'' +
                ", hangVe='" + hangVe + '\'' +
                '}';
    }
    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maCB);
        dest.writeString(maVe);
        dest.writeString(loaiVe);
        dest.writeString(soLuong);
        dest.writeString(soLuongCon);
        dest.writeString(hangVe);
    }
}
