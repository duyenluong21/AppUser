package com.example.app_user.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.app_user.fragment.FlightFragment;
import com.example.app_user.fragment.FlightedFragment;

public class MyViewPager extends FragmentStatePagerAdapter {


    public MyViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new FlightFragment() ;
            case 1 :
                return new FlightedFragment() ;
            default:
                return new FlightedFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "" ;
        switch (position){
            case 0 :
                title = "SẮP BAY";
                break;
            case 1 :
                title = "ĐÃ BAY" ;
                break;
        }
        return title;
    }
}
