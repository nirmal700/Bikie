package com.bikie.in.Singup_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bikie.in.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {
    private TextInputLayout et_userName, et_phoneNumber, et_password;
    Button btn_getOtp, btn_login;

    ProgressDialog progressDialog;
    RadioButton rb_selectedGender;
    RadioGroup rg_gender;

    //FireBase Variables
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_userName = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        btn_getOtp = findViewById(R.id.btn_getOtp);
        btn_login = findViewById(R.id.btn_backToLogin);
        rg_gender = findViewById(R.id.radio_group);

        auth = FirebaseAuth.getInstance();

        //--------------- Internet Checking -----------
        if (!isConnected(Signup.this)){
            showCustomDialog();
        }

        btn_login.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //EditText Validations
                if (!validatePhoneNumber() | !validateUserName() | !validateGender() |!validatePassword() ) {

                    return;
                }

                //Initialize ProgressDialog
                progressDialog = new ProgressDialog(Signup.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                rb_selectedGender = findViewById(rg_gender.getCheckedRadioButtonId());


                String phone = Objects.requireNonNull(et_phoneNumber.getEditText()).getText().toString().trim();
                String phoneNumber = "+91" + phone;

                if (!phone.isEmpty()) {
                    if (phone.length() == 10) {

                        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber);

                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Signup.this, "This User already Exist  Please Login", Toast.LENGTH_LONG).show();

                                } else {


                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(Signup.this)
                                            .setCallbacks(mCallBacks)
                                            .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Signup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Signup.this, "Please Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Signup.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code

                new Handler().postDelayed(() -> {

                    Intent otpIntent = new Intent(Signup.this, OtpVerification.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    otpIntent.putExtra("auth", s);

                    String phoneNumber = "+91" + Objects.requireNonNull(et_phoneNumber.getEditText()).getText().toString();
                    String name = Objects.requireNonNull(et_userName.getEditText()).getText().toString();
                    String password = Objects.requireNonNull(et_password.getEditText()).getText().toString();
                    String gender = rb_selectedGender.getText().toString();

                    otpIntent.putExtra("phoneNumber", phoneNumber);
                    otpIntent.putExtra("name", name);
                    otpIntent.putExtra("password", password);
                    otpIntent.putExtra("gender", gender);

                    startActivity(otpIntent);
                    finish();
                }, 1);

            }
        };
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    private boolean validatePhoneNumber() {

        String val = Objects.requireNonNull(et_phoneNumber.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_phoneNumber.setError("Field can not be empty");
            return false;
        } else if (val.length() > 10 | val.length() < 10) {
            et_phoneNumber.setError("Please Enter 10 Digit Phone Number");
            return false;
        } else if (!val.matches("\\w*")) {
            et_phoneNumber.setError("White spaces not allowed");
            return false;
        } else {
            et_phoneNumber.setError(null);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = Objects.requireNonNull(et_userName.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_userName.setError("Field can not be empty");
            return false;
        } else if (val.length() > 25) {
            et_userName.setError("Name is Too Large");
            return false;
        } else {
            et_userName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_password.setError("Field can not be empty");
            return false;
        } else if (val.length() < 8) {
            et_password.setError("Password minimum 8 Characters");
            return false;
        } else if (!val.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            et_password.setError("Must contain atleast one letter and one number");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }

    }
    private boolean validateGender(){

        if (rg_gender.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }
    //--------------- Internet Error Dialog Box -----------
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
        builder.setMessage("Please connect to the internet")
                //   .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),Signup.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(Signup userSignUp) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userSignUp.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
}