package com.globalshops.customer.shoeShop.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoeViewPager implements Parcelable {
    private String imageUrl;

    public ShoeViewPager() {
    }

    public ShoeViewPager(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    protected ShoeViewPager(Parcel in) {
        imageUrl = in.readString();
    }

    public static final Creator<ShoeViewPager> CREATOR = new Creator<ShoeViewPager>() {
        @Override
        public ShoeViewPager createFromParcel(Parcel in) {
            return new ShoeViewPager(in);
        }

        @Override
        public ShoeViewPager[] newArray(int size) {
            return new ShoeViewPager[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
    }
}
