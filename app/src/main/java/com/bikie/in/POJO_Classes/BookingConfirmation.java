package com.bikie.in.POJO_Classes;

import com.google.firebase.Timestamp;

public class BookingConfirmation {
    String mAmount,mMerchantTransactionID,bankId,bankTransactionId,pgTransactionId,mPaymentInstrumentType,mPhonepeTransactionID,mResponseCode,mVehicleID,mVehicleImage,mVehicleName,mVehicleLocation,mBookingID,mUserName,mUserPhoneNo,mUserAadharNo,mUserDlNo,mUserAadharFPIMG,mUserAadharBPIMG,muserDLIMG;
    Boolean isBookingSuccessfull, isPaymentSuccessfull;
    Timestamp mPickupDate,mDropOffDate,mBookingAttemptedTime,mBookingCompletedTime;
    String mHelmetRentalCharge,mVehicleRentalCharge,mSubTotal;

    public BookingConfirmation() {
    }

    public BookingConfirmation(String mAmount, String mMerchantTransactionID, String bankId, String bankTransactionId, String pgTransactionId, String mPaymentInstrumentType, String mPhonepeTransactionID, String mResponseCode, String mVehicleID, String mVehicleImage, String mVehicleName, String mVehicleLocation, Boolean isBookingSuccessfull, Boolean isPaymentSuccessfull, Timestamp mPickupDate, Timestamp mDropOffDate, Timestamp mBookingAttemptedTime, Timestamp mBookingCompletedTime, String mHelmetRentalCharge, String mVehicleRentalCharge, String mSubTotal, String mBookingID,String mUserName,String mUserPhoneNo,String mUserAadharNo, String mUserDlNo,String mUserAadharFPIMG,String mUserAadharBPIMG,String muserDLIMG) {
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
        this.mUserName = mUserName;
        this.mUserPhoneNo = mUserPhoneNo;
        this.mUserAadharNo = mUserAadharNo;
        this.mUserDlNo = mUserDlNo;
        this.mUserAadharFPIMG = mUserAadharFPIMG;
        this.mUserAadharBPIMG = mUserAadharBPIMG;
        this.muserDLIMG = muserDLIMG;
    }

    public String getmUserAadharNo() {
        return mUserAadharNo;
    }

    public void setmUserAadharNo(String mUserAadharNo) {
        this.mUserAadharNo = mUserAadharNo;
    }

    public String getmUserDlNo() {
        return mUserDlNo;
    }

    public void setmUserDlNo(String mUserDlNo) {
        this.mUserDlNo = mUserDlNo;
    }

    public String getmUserAadharFPIMG() {
        return mUserAadharFPIMG;
    }

    public void setmUserAadharFPIMG(String mUserAadharFPIMG) {
        this.mUserAadharFPIMG = mUserAadharFPIMG;
    }

    public String getmUserAadharBPIMG() {
        return mUserAadharBPIMG;
    }

    public void setmUserAadharBPIMG(String mUserAadharBPIMG) {
        this.mUserAadharBPIMG = mUserAadharBPIMG;
    }

    public String getMuserDLIMG() {
        return muserDLIMG;
    }

    public void setMuserDLIMG(String muserDLIMG) {
        this.muserDLIMG = muserDLIMG;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserPhoneNo() {
        return mUserPhoneNo;
    }

    public void setmUserPhoneNo(String mUserPhoneNo) {
        this.mUserPhoneNo = mUserPhoneNo;
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

    public String getmHelmetRentalCharge() {
        return mHelmetRentalCharge;
    }

    public void setmHelmetRentalCharge(String mHelmetRentalCharge) {
        this.mHelmetRentalCharge = mHelmetRentalCharge;
    }

    public String getmVehicleRentalCharge() {
        return mVehicleRentalCharge;
    }

    public void setmVehicleRentalCharge(String mVehicleRentalCharge) {
        this.mVehicleRentalCharge = mVehicleRentalCharge;
    }

    public String getmSubTotal() {
        return mSubTotal;
    }

    public void setmSubTotal(String mSubTotal) {
        this.mSubTotal = mSubTotal;
    }

    public String getmBookingID() {
        return mBookingID;
    }

    public void setmBookingID(String mBookingID) {
        this.mBookingID = mBookingID;
    }
}
