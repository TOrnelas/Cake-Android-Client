package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tiagoornelas on 13/02/2018.
 */

public class Cake implements Parcelable{

    private String title, description;
    private String imageUrl;
    private Bitmap imageBitmap;

    public Cake(String title, String description, String imageUrl) {

        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    //Parcelable implementation

    protected Cake(Parcel in) {

        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Cake> CREATOR = new Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeParcelable(imageBitmap, flags);
    }
}