package com.example.app_user.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class User implements Parcelable {
    private String maNV;
    private String hoNV;
    private String tenNV;
    private String ngaySinhNV;
    private String sdtNV;
    private String chucVu;
    private String trinhDoHocVan;
    private String kinhNghiem;
    private String trangThaiHoatDong;
    private String username;
    private String passw;
    private String salt ;

    public User(String username) {
        this.username = username;
    }


    public User(String user, String maNV,String hoNV,
                String tenNV, String ngaySinhNV, String sdtNV, String chucVu, String trinhDoHocVan, String kinhNghiem ) {
        this.username = user;
        this.maNV = maNV;
        this.hoNV = hoNV;
        this.tenNV = tenNV;
        this.ngaySinhNV = ngaySinhNV;
        this.sdtNV = sdtNV;
        this.chucVu = chucVu;
        this.trinhDoHocVan = trinhDoHocVan;
        this.kinhNghiem = kinhNghiem;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public User(String username, String passw) {
        this.username = username;
        this.passw = passw;
    }
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoNV() {
        return hoNV;
    }

    public void setHoNV(String hoNV) {
        this.hoNV = hoNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getNgaySinhNV() {
        return ngaySinhNV;
    }

    public void setNgaySinhNV(String ngaySinhNV) {
        this.ngaySinhNV = ngaySinhNV;
    }

    public String getSdtNV() {
        return sdtNV;
    }

    public void setSdtNV(String sdtNV) {
        this.sdtNV = sdtNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getTrinhDoHocVan() {
        return trinhDoHocVan;
    }

    public void setTrinhDoHocVan(String trinhDoHocVan) {
        this.trinhDoHocVan = trinhDoHocVan;
    }

    public String getKinhNghiem() {
        return kinhNghiem;
    }

    public void setKinhNghiem(String kinhNghiem) {
        this.kinhNghiem = kinhNghiem;
    }

    public String getTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(String trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public User(@NonNull Parcel in) {
        maNV = in.readString();
        hoNV = in.readString();
        tenNV = in.readString();
        ngaySinhNV = in.readString();
        sdtNV = in.readString();
        chucVu = in.readString();
        trinhDoHocVan = in.readString();
        kinhNghiem = in.readString();
        trangThaiHoatDong = in.readString();
        username = in.readString();
        passw = in.readString();
        salt = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(maNV);
        dest.writeString(hoNV);
        dest.writeString(tenNV);
        dest.writeString(ngaySinhNV);
        dest.writeString(sdtNV);
        dest.writeString(chucVu);
        dest.writeString(trinhDoHocVan);
        dest.writeString(kinhNghiem);
        dest.writeString(trangThaiHoatDong);
        dest.writeString(username);
        dest.writeString(passw);
        dest.writeString(salt);
    }
}
