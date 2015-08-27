package com.example.raghawendrakumar.quikrcars;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raghawendra.kumar on 23-08-2015.
 */
public class CarDetail implements Parcelable {

    String name;
    String image;
    String price;
    String brand;
    String type;
    String rating;
    String color;
    String engine_cc;
    String mileage;
    String abs_exist;
    String description;
    String link;
    City [] cities;


    public CarDetail(JSONObject carDetail) {
        try {
            this.name = carDetail.getString("name");
            this.image = carDetail.getString("image");
            this.price = carDetail.getString("price");
            this.brand = carDetail.getString("brand");
            this.type = carDetail.getString("type");
            this.rating = carDetail.getString("rating");
            this.color = carDetail.getString("color");
            this.engine_cc = carDetail.getString("engine_cc");
            this.mileage = carDetail.getString("mileage");
            this.abs_exist = carDetail.getString("abs_exist");
            this.description = carDetail.getString("description");
            this.link = carDetail.getString("link");
            JSONArray city = carDetail.getJSONArray("cities");
            cities = new City[city.length()];
            for (int i = 0; i < city.length(); i++) {
                JSONObject carDetails = city.getJSONObject(i);
                cities[i] = new City(carDetails.getString("city"),carDetails.getString("users"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected CarDetail(Parcel in) {
        name = in.readString();
        image = in.readString();
        price = in.readString();
        brand = in.readString();
        type = in.readString();
        rating = in.readString();
        color = in.readString();
        engine_cc = in.readString();
        mileage = in.readString();
        abs_exist = in.readString();
        description = in.readString();
        link = in.readString();
        cities = in.createTypedArray(City.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(price);
        dest.writeString(brand);
        dest.writeString(type);
        dest.writeString(rating);
        dest.writeString(color);
        dest.writeString(engine_cc);
        dest.writeString(mileage);
        dest.writeString(abs_exist);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeTypedArray(cities, 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CarDetail> CREATOR = new Parcelable.Creator<CarDetail>() {
        @Override
        public CarDetail createFromParcel(Parcel in) {
            return new CarDetail(in);
        }

        @Override
        public CarDetail[] newArray(int size) {
            return new CarDetail[size];
        }
    };
}
