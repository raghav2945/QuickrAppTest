package com.example.raghawendrakumar.quikrcars;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raghawendra.kumar on 23-08-2015.
 */
public class City implements Parcelable {

    String cityName;
    String cityUser;

    public City(String cityName, String cityUser) {
        this.cityName = cityName;
        this.cityUser = cityUser;
    }

    protected City(Parcel in) {
        cityName = in.readString();
        cityUser = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(cityUser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}