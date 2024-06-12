package com.example.app_user.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("ParcelCreator")
public class Chat implements Parcelable {
    String maKH ;
    String maTN;
    String fullname;
    String noiDung1;
    String noiDung2;
    String thoiGianGui;

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public Chat(String maTN) {
        this.maTN = maTN;
    }

    public String getMaTN() {
        return maTN;
    }

    public void setMaTN(String maTN) {
        this.maTN = maTN;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNoiDung1() {
        return noiDung1;
    }

    public void setNoiDung1(String noiDung1) {
        this.noiDung1 = noiDung1;
    }

    public String getNoiDung2() {
        return noiDung2;
    }

    public void setNoiDung2(String noiDung2) {
        this.noiDung2 = noiDung2;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "maTN='" + maTN + '\'' +
                ", fullname='" + fullname + '\'' +
                ", noiDung1='" + noiDung1 + '\'' +
                ", noiDung2='" + noiDung2 + '\'' +
                ", thoiGianGui='" + thoiGianGui + '\'' +
                '}';
    }

    public String getThoiGianGui() {
        return thoiGianGui;
    }

    public void setThoiGianGui(String thoiGianGui) {
        this.thoiGianGui = thoiGianGui;
    }
    protected Chat(Parcel in) {
        maTN = in.readString();
        noiDung1 = in.readString();
        thoiGianGui = in.readString();
        fullname = in.readString();
        noiDung2 = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(maTN);
        dest.writeString(noiDung1);
        dest.writeString(thoiGianGui);
        dest.writeString(fullname);
        dest.writeString(noiDung2);
    }
}
