package com.bikie.in.POJO_Classes;

import com.google.firebase.Timestamp;

public class BookingConfirmation {
    String mAmount,mMerchantTransactionID,bankId,bankTransactionId,pgTransactionId,mPaymentInstrumentType,mPhonepeTransactionID,mResponseCode,mVehicleID,mVehicleImage,mVehicleName,mVehicleLocation,mBookingID;
    Boolean isBookingSuccessfull, isPaymentSuccessfull;
    Timestamp mPickupDate,mDropOffDate,mBookingAttemptedTime,mBookingCompletedTime;
    Double mHelmetRentalCharge,mVehicleRentalCharge,mSubTotal;

    public BookingConfirmation() {
    }

    public BookingConfirmation(String mAmount, String mMerchantTransactionID, String bankId, String bankTransactionId, String pgTransactionId, String mPaymentInstrumentType, String mPhonepeTransactionID, String mResponseCode, String mVehicleID, String mVehicleImage, String mVehicleName, String mVehicleLocation, Boolean isBookingSuccessfull, Boolean isPaymentSuccessfull, Timestamp mPickupDate, Timestamp mDropOffDate, Timestamp mBookingAttemptedTime, Timestamp mBookingCompletedTime, Double mHelmetRentalCharge, Double mVehicleRentalCharge, Double mSubTotal, String mBookingID) {
        this.mAmount = mAmount;
        this.mMerchantTransactionID = mMerchantTransactionID;
        this.bankId = bankId;
        this.bankTransactionId = bankTransactionId;
        this.pgTransactionId = pgTransactionId;
        this.mPaymentInstrumentType = mPaymentInstrumentType;
        this.mPhonepeTransactionID = mPhonepeTransactionID;
        this.mResponseCode = mResponseCode;
        this.mVehicleID = mVehicleID;
        this.mVehicleImage = mVehicleImage;
        this.mVehicleName = mVehicleName;
        this.mVehicleLocation = mVehicleLocation;
        this.isBookingSuccessfull = isBookingSuccessfull;
        this.isPaymentSuccessfull = isPaymentSuccessfull;
        this.mPickupDate = mPickupDate;
        this.mDropOffDate = mDropOffDate;
        this.mBookingAttemptedTime = mBookingAttemptedTime;
        this.mBookingCompletedTime = mBookingCompletedTime;
        this.mHelmetRentalCharge = mHelmetRentalCharge;
        this.mVehicleRentalCharge = mVehicleRentalCharge;
        this.mSubTotal = mSubTotal;
        this.mBookingID = mBookingID;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmMerchantTransactionID() {
        return mMerchantTransactionID;
    }

    public void setmMerchantTransactionID(String mMerchantTransactionID) {
        this.mMerchantTransactionID = mMerchantTransactionID;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getPgTransactionId() {
        return pgTransactionId;
    }

    public void setPgTransactionId(String pgTransactionId) {
        this.pgTransactionId = pgTransactionId;
    }

    public String getmPaymentInstrumentType() {
        return mPaymentInstrumentType;
    }

    public void setmPaymentInstrumentType(String mPaymentInstrumentType) {
        this.mPaymentInstrumentType = mPaymentInstrumentType;
    }

    public String getmPhonepeTransactionID() {
        return mPhonepeTransactionID;
    }

    public void setmPhonepeTransactionID(String mPhonepeTransactionID) {
        this.mPhonepeTransactionID = mPhonepeTransactionID;
    }

    public String getmResponseCode() {
        return mResponseCode;
    }

    public void setmResponseCode(String mResponseCode) {
        this.mResponseCode = mResponseCode;
    }

    public String getmVehicleID() {
        return mVehicleID;
    }

    public void setmVehicleID(String mVehicleID) {
        this.mVehicleID = mVehicleID;
    }

    public String getmVehicleImage() {
        return mVehicleImage;
    }

    public void setmVehicleImage(String mVehicleImage) {
        this.mVehicleImage = mVehicleImage;
    }

    public String getmVehicleName() {
        return mVehicleName;
    }

    public void setmVehicleName(String mVehicleName) {
        this.mVehicleName = mVehicleName;
    }

    public String getmVehicleLocation() {
        return mVehicleLocation;
    }

    public void setmVehicleLocation(String mVehicleLocation) {
        this.mVehicleLocation = mVehicleLocation;
    }

    public Boolean getBookingSuccessfull() {
        return isBookingSuccessfull;
    }

    public void setBookingSuccessfull(Boolean bookingSuccessfull) {
        isBookingSuccessfull = bookingSuccessfull;
    }

    public Boolean getPaymentSuccessfull() {
        return isPaymentSuccessfull;
    }

    public void setPaymentSuccessfull(Boolean paymentSuccessfull) {
        isPaymentSuccessfull = paymentSuccessfull;
    }

    public Timestamp getmPickupDate() {
        return mPickupDate;
    }

    public void setmPickupDate(Timestamp mPickupDate) {
        this.mPickupDate = mPickupDate;
    }

    public Timestamp getmDropOffDate() {
        return mDropOffDate;
    }

    public void setmDropOffDate(Timestamp mDropOffDate) {
        this.mDropOffDate = mDropOffDate;
    }

    public Timestamp getmBookingAttemptedTime() {
        return mBookingAttemptedTime;
    }

    public void setmBookingAttemptedTime(Timestamp mBookingAttemptedTime) {
        this.mBookingAttemptedTime = mBookingAttemptedTime;
    }

    public Timestamp getmBookingCompletedTime() {
        return mBookingCompletedTime;
    }

    public void setmBookingCompletedTime(Timestamp mBookingCompletedTime) {
        this.mBookingCompletedTime = mBookingCompletedTime;
    }

    public Double getmHelmetRentalCharge() {
        return mHelmetRentalCharge;
    }

    public void setmHelmetRentalCharge(Double mHelmetRentalCharge) {
        this.mHelmetRentalCharge = mHelmetRentalCharge;
    }

    public Double getmVehicleRentalCharge() {
        return mVehicleRentalCharge;
    }

    public void setmVehicleRentalCharge(Double mVehicleRentalCharge) {
        this.mVehicleRentalCharge = mVehicleRentalCharge;
    }

    public Double getmSubTotal() {
        return mSubTotal;
    }

    public void setmSubTotal(Double mSubTotal) {
        this.mSubTotal = mSubTotal;
    }

    public String getmBookingID() {
        return mBookingID;
    }

    public void setmBookingID(String mBookingID) {
        this.mBookingID = mBookingID;
    }
}
