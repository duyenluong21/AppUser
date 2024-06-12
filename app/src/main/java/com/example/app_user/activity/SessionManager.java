package com.example.app_user.activity;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.app_user.model.User;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MAKH = "maKH";
    private static final String KEY_MANV = "maNV";
    private static final String KEY_HONV = "hoNV";
    private static final String KEY_TENNV = "tenNV";
    private static final String KEY_NGAYSINH = "ngaySinhNV";
    private static final String KEY_SDT = "sdtNV";
    private static final String KEY_CHUCVU = "chucVu";

    private static final String KEY_TRINHDO = "trinhDo";
    private static final String KEY_KINHNGHIEM = "kinhNghiem";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void saveUser(User user) {
        // Lưu thông tin người dùng vào SharedPreferences
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_MANV, user.getMaNV());
        editor.putString(KEY_HONV, user.getHoNV());
        editor.putString(KEY_TENNV, user.getTenNV());
        editor.putString(KEY_NGAYSINH, user.getNgaySinhNV());
        editor.putString(KEY_SDT, user.getSdtNV());
        editor.putString(KEY_CHUCVU, user.getChucVu());
        editor.putString(KEY_TRINHDO, user.getTrinhDoHocVan());
        editor.putString(KEY_KINHNGHIEM, user.getKinhNghiem());
        // Thêm các thông tin khác nếu cần
        editor.apply();
    }

    public User getUser() {
        String user = sharedPreferences.getString(KEY_USERNAME, "");
        String maNV = sharedPreferences.getString(KEY_MANV, "");
        String hoNV = sharedPreferences.getString(KEY_HONV, "");
        String tenNV = sharedPreferences.getString(KEY_TENNV, "");
        String ngaySinhNV = sharedPreferences.getString(KEY_NGAYSINH, "");
        String sdtNV = sharedPreferences.getString(KEY_SDT, "");
        String chucVu = sharedPreferences.getString(KEY_CHUCVU, "");
        String trinhDoHocVan = sharedPreferences.getString(KEY_TRINHDO, "");
        String kinhNghiem = sharedPreferences.getString(KEY_KINHNGHIEM, "");

        return new User(user, maNV,hoNV, tenNV, ngaySinhNV, sdtNV, chucVu, trinhDoHocVan, kinhNghiem);
    }

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void saveLoginDetails(String username, String maNV, String hoNV,String tenNV,String ngaySinhNV, String sdtNV,
                                 String chucVu, String trinhDoHocVan, String kinhNghiem ) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_MANV, maNV);
        editor.putString(KEY_HONV, hoNV);
        editor.putString(KEY_TENNV, tenNV);
        editor.putString(KEY_NGAYSINH, ngaySinhNV);
        editor.putString(KEY_SDT, sdtNV);
        editor.putString(KEY_CHUCVU, chucVu);
        editor.putString(KEY_TRINHDO, trinhDoHocVan);
        editor.putString(KEY_KINHNGHIEM, kinhNghiem);
        editor.apply();
    }


    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USERNAME);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

}
