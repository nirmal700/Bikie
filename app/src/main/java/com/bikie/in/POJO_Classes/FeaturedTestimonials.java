package com.bikie.in.POJO_Classes;

public class FeaturedTestimonials {
    String mTestimonialImageURL,mUserName,mReview;

    public FeaturedTestimonials() {
    }

    public FeaturedTestimonials(String mTestimonialImageURL, String mUserName, String mReview) {
        this.mTestimonialImageURL = mTestimonialImageURL;
        this.mUserName = mUserName;
        this.mReview = mReview;
    }

    public String getmTestimonialImageURL() {
        return mTestimonialImageURL;
    }

    public void setmTestimonialImageURL(String mTestimonialImageURL) {
        this.mTestimonialImageURL = mTestimonialImageURL;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }
}
