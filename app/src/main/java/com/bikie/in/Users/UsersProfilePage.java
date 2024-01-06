package com.bikie.in.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

public class UsersProfilePage extends AppCompatActivity {
    private TextInputLayout mUserName,mUserPhoneNumber,mAadharNo,mDlNo;
    private ImageView mAadharFront,mAadharBack,mDlURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile_page);

        mUserName = findViewById(R.id.et_vName);
        mUserPhoneNumber = findViewById(R.id.et_vPhone);
        mAadharNo = findViewById(R.id.et_vAadharAddress);
        mDlNo = findViewById(R.id.et_DrivingLicenseNo);
        mAadharFront = findViewById(R.id.img_Aadhar_Front);
        mAadharBack = findViewById(R.id.img_Aadhar_Back);
        mDlURL = findViewById(R.id.img_DrivingLicense);

        SessionManager manager = new SessionManager(UsersProfilePage.this);

        mUserName.getEditText().setText(manager.getName());
        mUserPhoneNumber.getEditText().setText(manager.getPhone());
        mAadharNo.getEditText().setText(manager.getAadharNo());
        mDlNo.getEditText().setText(manager.getDlNo());
        mUserName.getEditText().setText(manager.getName());

        Glide.with(UsersProfilePage.this).load(manager.getAadharFrontURL())
//                .placeholder(R.drawable.sand_clock)
                .into(mAadharFront);
        Glide.with(UsersProfilePage.this).load(manager.getAadharBackURL())
//                .placeholder(R.drawable.sand_clock)
                .into(mAadharBack);
        Glide.with(UsersProfilePage.this).load(manager.getDlImageURL())
//                .placeholder(R.drawable.sand_clock)
                .into(mDlURL);
    }
}