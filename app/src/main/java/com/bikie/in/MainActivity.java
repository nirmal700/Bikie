package com.bikie.in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bikie.in.Singup_Login.Signup;
import com.bikie.in.Singup_Login.SignupAadhar;
import com.bikie.in.Singup_Login.SingupDL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, Signup.class);
        startActivity(intent);
        finish();
    }
}