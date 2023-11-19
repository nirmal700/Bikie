package com.bikie.in.POJO_Classes;

public class FeaturedBikes {
    String mImageURL,mBikeName,mDescription;

    public FeaturedBikes() {
    }

    public FeaturedBikes(String mImageURL, String mBikeName, String mDescription) {
        this.mImageURL = mImageURL;
        this.mBikeName = mBikeName;
        this.mDescription = mDescription;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getmBikeName() {
        return mBikeName;
    }

    public void setmBikeName(String mBikeName) {
        this.mBikeName = mBikeName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
