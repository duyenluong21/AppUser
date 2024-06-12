package com.example.app_user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable {
    private String maCB;
    private String maDB;
    private String maMB;
    private String ngayDi;
    private String ngayDen;
    private String diaDiemDi;
    private String diaDiemDen;
    private String giaVe;
    private String ghiChu;
    private String gioBay;


    public String getMaCB() {
        return maCB;
    }

    public void setMaCB(String maCB) {
        this.maCB = maCB;
    }

    public String getMaDB() {
        return maDB;
    }

    public void setMaDB(String maDB) {
        this.maDB = maDB;
    }

    public String getMaMB() {
        return maMB;
    }

    public void setMaMB(String maMB) {
        this.maMB = maMB;
    }

    public String getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(String ngayDi) {
        this.ngayDi = ngayDi;
    }

    public String getNgayDen() {
        return ngayDen;
    }

    public void setNgayDen(String ngayDen) {
        this.ngayDen = ngayDen;
    }

    public String getDiaDiemDi() {
        return diaDiemDi;
    }

    public void setDiaDiemDi(String diaDiemDi) {
        this.diaDiemDi = diaDiemDi;
    }

    public String getDiaDiemDen() {
        return diaDiemDen;
    }

    public void setDiaDiemDen(String diaDiemDen) {
        this.diaDiemDen = diaDiemDen;
    }

    public String getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(String giaVe) {
        this.giaVe = giaVe;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getGioBay() {
        return gioBay;
    }

    public void setGioBay(String gioBay) {
        this.gioBay = gioBay;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "maCB='" + maCB + '\'' +
                ", maMB='" + maMB + '\'' +
                ", maDB='" + maDB + '\'' +
                ", ngayDi='" + ngayDi + '\'' +
                ", ngayDen='" + ngayDen + '\'' +
                ", diaDiemDi='" + diaDiemDi + '\'' +
                ", diaDiemDen='" + diaDiemDen + '\'' +
                ", giaVe='" + giaVe + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", gioBay='" + gioBay + '\'' +
                '}';
    }
    protected Flight(Parcel in) {
        maCB = in.readString();
        maDB = in.readString();
        maMB = in.readString();
        ngayDi = in.readString();
        ngayDen = in.readString();
        diaDiemDi = in.readString();
        diaDiemDen = in.readString();
        giaVe = in.readString();
        ghiChu = in.readString();
        gioBay = in.readString();
    }

    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maCB);
        dest.writeString(maDB);
        dest.writeString(maMB);
        dest.writeString(ngayDi);
        dest.writeString(ngayDen);
        dest.writeString(diaDiemDi);
        dest.writeString(diaDiemDen);
        dest.writeString(giaVe);
        dest.writeString(ghiChu);
        dest.writeString(gioBay);
    }
}
