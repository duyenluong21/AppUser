package com.example.app_user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class passenger implements Parcelable {
    private String fullname;
    private String gioiTinh;

    private String ngaySinh;
    private String soCCCD;
    private String diaChi;
    private String soDT;
    private String loaiHanhKhach;
    private String tong;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getLoaiHanhKhach() {
        return loaiHanhKhach;
    }

    public void setLoaiHanhKhach(String loaiKhachHang) {
        this.loaiHanhKhach = loaiKhachHang;
    }

    public String getTong() {
        return tong;
    }

    public void setTong(String tong) {
        this.tong = tong;
    }

    @Override
    public String toString() {
        return "passenger{" +
                ", fullname='" + fullname + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngaySinh='" + ngaySinh + '\'' +
                ", soCCCD='" + soCCCD + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDT='" + soDT + '\'' +
                ", loaiHanhKhach='" + loaiHanhKhach + '\'' +
                ", tong='" + tong + '\'' +
                '}';
    }
    protected passenger(Parcel in) {
        fullname = in.readString();
        gioiTinh = in.readString();
        ngaySinh = in.readString();
        soCCCD = in.readString();
        diaChi = in.readString();
        soDT = in.readString();
        loaiHanhKhach = in.readString();
        tong = in.readString();
    }

    public static final Creator<passenger> CREATOR = new Creator<passenger>() {
        @Override
        public passenger createFromParcel(Parcel in) {
            return new passenger(in);
        }

        @Override
        public passenger[] newArray(int size) {
            return new passenger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullname);
        dest.writeString(gioiTinh);
        dest.writeString(ngaySinh);
        dest.writeString(soCCCD);
        dest.writeString(diaChi);
        dest.writeString(soDT);
        dest.writeString(loaiHanhKhach);
        dest.writeString(tong);
    }
}
