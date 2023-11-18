package com.bikie.in.Singup_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.bikie.in.Dashboard;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class Login extends AppCompatActivity {

    SessionManager manager;

    ProgressDialog progressDialog;

    TextInputLayout et_phoneNumber, et_password;
    Button btn_login, btn_backSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_backSignUp = findViewById(R.id.btn_backSignUp);
        //Create a Session
        manager = new SessionManager(getApplicationContext());

        btn_backSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Signup.class)));

        btn_login.setOnClickListener(v -> userLogin());
    }

    private void userLogin() {
        if (!validatePhoneNumber() | !validatePassword()) {

            return;
        }
        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String _phoneNumber = Objects.requireNonNull(et_phoneNumber.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();

        if (_phoneNumber.charAt(0) == '0') {

            _phoneNumber = _phoneNumber.substring(1);
        }

        String _completePhoneNumber = "+91" + _phoneNumber;

        // DataBase Check Query
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) { //Check User

                    et_phoneNumber.getEditText().setError(null);
                    String systemPassword = snapshot.child(_completePhoneNumber).child("Profile").child("password").getValue(String.class);

                    assert systemPassword != null;
                    if (checkPassword(_password,systemPassword)) {
                        et_phoneNumber.getEditText().setError(null);

                        //Get User data From DataBase
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("Profile").child("phoneNumber").getValue(String.class);
                        String _password = snapshot.child(_completePhoneNumber).child("Profile").child("password").getValue(String.class);
                        String _name = snapshot.child(_completePhoneNumber).child("Profile").child("name").getValue(String.class);
                        String _aadharNo = snapshot.child(_completePhoneNumber).child("Profile").child("aadharNo").getValue(String.class);
                        String _dlNo = snapshot.child(_completePhoneNumber).child("Profile").child("dlNo").getValue(String.class);
                        String _dlIMGURL = snapshot.child(_completePhoneNumber).child("Profile").child("dlImageURL").getValue(String.class);
                        String _aadharFront = snapshot.child(_completePhoneNumber).child("Profile").child("aadharFrontURL").getValue(String.class);
                        String _aadharBack = snapshot.child(_completePhoneNumber).child("Profile").child("aadharBackURL").getValue(String.class);



                        manager.setUserLogin(true); //Set User Login Session
                        manager.setDetails(_phoneNo, et_password.getEditText().getText().toString().trim(),_name,_dlNo,_aadharNo,_dlIMGURL,_aadharFront,_aadharBack); //Add Data To User Session manager
                        // Intent to Next Activity
                        startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "User Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(et_password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            et_password.setError("Field can not be empty");
            return false;
        } else if (!val.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            et_password.setError("Must contain atleast one letter and one number");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
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
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}