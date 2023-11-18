package com.bikie.in.POJO_Classes;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.Timestamp;



public class NewVehicle {
    private String mVehicleId,mVehicleName,mVehicleNo,mVehicleInfo,mVehicleLocation,mVehicleCategory;
    private GeoPoint mVehicleLatLong;
    private int mVehicleTopSpeed, mVehicleMileage, mVehicleRent1Hr,mVehicleRent2Hr, mVehicleRent4Hr, mVehicleRent12Hr, mVehicleRent24Hr,mVehicleCC;

    private Boolean mIsInPending, mIsAvailable;
    private Timestamp mBookingAttemptedTime;

    private ArrayList<String> mVehicleImages;

    public NewVehicle() {
    }

    public NewVehicle(String mVehicleId, String mVehicleName, String mVehicleNo, String mVehicleInfo, String mVehicleLocation, String mVehicleCategory, GeoPoint mVehicleLatLong, int mVehicleTopSpeed, int mVehicleMileage, int mVehicleRent1Hr, int mVehicleRent2Hr, int mVehicleRent4Hr, int mVehicleRent12Hr, int mVehicleRent24Hr, int mVehicleCC, Boolean mIsInPending, Boolean mIsAvailable, Timestamp mBookingAttemptedTime, ArrayList<String> mVehicleImages) {
        this.mVehicleId = mVehicleId;
        this.mVehicleName = mVehicleName;
        this.mVehicleNo = mVehicleNo;
        this.mVehicleInfo = mVehicleInfo;
        this.mVehicleLocation = mVehicleLocation;
        this.mVehicleCategory = mVehicleCategory;
        this.mVehicleLatLong = mVehicleLatLong;
        this.mVehicleTopSpeed = mVehicleTopSpeed;
        this.mVehicleMileage = mVehicleMileage;
        this.mVehicleRent1Hr = mVehicleRent1Hr;
        this.mVehicleRent2Hr = mVehicleRent2Hr;
        this.mVehicleRent4Hr = mVehicleRent4Hr;
        this.mVehicleRent12Hr = mVehicleRent12Hr;
        this.mVehicleRent24Hr = mVehicleRent24Hr;
        this.mVehicleCC = mVehicleCC;
        this.mIsInPending = mIsInPending;
        this.mIsAvailable = mIsAvailable;
        this.mBookingAttemptedTime = mBookingAttemptedTime;
        this.mVehicleImages = mVehicleImages;
    }

    public String getmVehicleId() {
        return mVehicleId;
    }

    public void setmVehicleId(String mVehicleId) {
        this.mVehicleId = mVehicleId;
    }

    public String getmVehicleName() {
        return mVehicleName;
    }

    public void setmVehicleName(String mVehicleName) {
        this.mVehicleName = mVehicleName;
    }

    public String getmVehicleNo() {
        return mVehicleNo;
    }

    public void setmVehicleNo(String mVehicleNo) {
        this.mVehicleNo = mVehicleNo;
    }

    public String getmVehicleInfo() {
        return mVehicleInfo;
    }

    public void setmVehicleInfo(String mVehicleInfo) {
        this.mVehicleInfo = mVehicleInfo;
    }

    public String getmVehicleLocation() {
        return mVehicleLocation;
    }

    public void setmVehicleLocation(String mVehicleLocation) {
        this.mVehicleLocation = mVehicleLocation;
    }

    public String getmVehicleCategory() {
        return mVehicleCategory;
    }

    public void setmVehicleCategory(String mVehicleCategory) {
        this.mVehicleCategory = mVehicleCategory;
    }

    public GeoPoint getmVehicleLatLong() {
        return mVehicleLatLong;
    }

    public void setmVehicleLatLong(GeoPoint mVehicleLatLong) {
        this.mVehicleLatLong = mVehicleLatLong;
    }

    public int getmVehicleTopSpeed() {
        return mVehicleTopSpeed;
    }

    public void setmVehicleTopSpeed(int mVehicleTopSpeed) {
        this.mVehicleTopSpeed = mVehicleTopSpeed;
    }

    public int getmVehicleMileage() {
        return mVehicleMileage;
    }

    public void setmVehicleMileage(int mVehicleMileage) {
        this.mVehicleMileage = mVehicleMileage;
    }

    public int getmVehicleRent1Hr() {
        return mVehicleRent1Hr;
    }

    public void setmVehicleRent1Hr(int mVehicleRent1Hr) {
        this.mVehicleRent1Hr = mVehicleRent1Hr;
    }

    public int getmVehicleRent2Hr() {
        return mVehicleRent2Hr;
    }

    public void setmVehicleRent2Hr(int mVehicleRent2Hr) {
        this.mVehicleRent2Hr = mVehicleRent2Hr;
    }

    public int getmVehicleRent4Hr() {
        return mVehicleRent4Hr;
    }

    public void setmVehicleRent4Hr(int mVehicleRent4Hr) {
        this.mVehicleRent4Hr = mVehicleRent4Hr;
    }

    public int getmVehicleRent12Hr() {
        return mVehicleRent12Hr;
    }

    public void setmVehicleRent12Hr(int mVehicleRent12Hr) {
        this.mVehicleRent12Hr = mVehicleRent12Hr;
    }

    public int getmVehicleRent24Hr() {
        return mVehicleRent24Hr;
    }

    public void setmVehicleRent24Hr(int mVehicleRent24Hr) {
        this.mVehicleRent24Hr = mVehicleRent24Hr;
    }

    public int getmVehicleCC() {
        return mVehicleCC;
    }

    public void setmVehicleCC(int mVehicleCC) {
        this.mVehicleCC = mVehicleCC;
    }

    public Boolean getmIsInPending() {
        return mIsInPending;
    }

    public void setmIsInPending(Boolean mIsInPending) {
        this.mIsInPending = mIsInPending;
    }

    public Boolean getmIsAvailable() {
        return mIsAvailable;
    }

    public void setmIsAvailable(Boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
    }

    public Timestamp getmBookingAttemptedTime() {
        return mBookingAttemptedTime;
    }

    public void setmBookingAttemptedTime(Timestamp mBookingAttemptedTime) {
        this.mBookingAttemptedTime = mBookingAttemptedTime;
    }

    public ArrayList<String> getmVehicleImages() {
        return mVehicleImages;
    }

    public void setmVehicleImages(ArrayList<String> mVehicleImages) {
        this.mVehicleImages = mVehicleImages;
    }
}
