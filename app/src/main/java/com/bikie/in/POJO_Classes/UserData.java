package com.bikie.in.POJO_Classes;

public class UserData {
    String name, phoneNumber, password,gender,aadharNo,aadharFrontURL,aadharBackURL,dlImageURL,dlNo;
    Boolean isAllowedToMCWOG,isAllowedToMCWG,isAllowedToLMV;

    public UserData() {
    }

    public UserData(String name, String phoneNumber, String password, String gender, String aadharNo, String aadharFrontURL, String aadharBackURL, String dlImageURL, String dlNo, Boolean isAllowedToMCWOG, Boolean isAllowedToMCWG, Boolean isAllowedToLMV) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender;
        this.aadharNo = aadharNo;
        this.aadharFrontURL = aadharFrontURL;
        this.aadharBackURL = aadharBackURL;
        this.dlImageURL = dlImageURL;
        this.dlNo = dlNo;
        this.isAllowedToMCWOG = isAllowedToMCWOG;
        this.isAllowedToMCWG = isAllowedToMCWG;
        this.isAllowedToLMV = isAllowedToLMV;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getAadharFrontURL() {
        return aadharFrontURL;
    }

    public void setAadharFrontURL(String aadharFrontURL) {
        this.aadharFrontURL = aadharFrontURL;
    }

    public String getAadharBackURL() {
        return aadharBackURL;
    }

    public void setAadharBackURL(String aadharBackURL) {
        this.aadharBackURL = aadharBackURL;
    }

    public String getdlImageURL() {
        return dlImageURL;
    }

    public void setdlImageURL(String dlURL) {
        this.dlImageURL = dlURL;
    }

    public String getDlNo() {
        return dlNo;
    }

    public void setDlNo(String dlNo) {
        this.dlNo = dlNo;
    }

    public Boolean getAllowedToMCWOG() {
        return isAllowedToMCWOG;
    }

    public void setAllowedToMCWOG(Boolean allowedToMCWOG) {
        isAllowedToMCWOG = allowedToMCWOG;
    }

    public Boolean getAllowedToMCWG() {
        return isAllowedToMCWG;
    }

    public void setAllowedToMCWG(Boolean allowedToMCWG) {
        isAllowedToMCWG = allowedToMCWG;
    }

    public Boolean getAllowedToLMV() {
        return isAllowedToLMV;
    }

    public void setAllowedToLMV(Boolean allowedToLMV) {
        isAllowedToLMV = allowedToLMV;
    }
}
