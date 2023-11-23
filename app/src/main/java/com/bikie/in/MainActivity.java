package com.bikie.in;

import static com.bikie.in.Singup_Login.Login.checkPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bikie.in.SessionManager.SessionManager;
import com.bikie.in.Singup_Login.Signup;
import com.bikie.in.Singup_Login.SignupAadhar;
import com.bikie.in.Singup_Login.SingupDL;
import com.bikie.in.Users.UserDashboard;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Thread timer;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance());

        timer = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    manager = new SessionManager(getApplicationContext());

                    if (manager.getUserLogin()) {
//                        FirebaseDatabase.getInstance().getReference("Users").child(manager.getPhone()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) { //Check User
//
//                                    String systemPassword = snapshot.child("password").getValue(String.class);
//
//                                    if (checkPassword(manager.getPassword(), systemPassword)) {
//                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
//                                        finish();
//                                    }
//                                    else {
//                                        Intent intent = new Intent(MainActivity.this, Signup.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }else {
//                                    Intent intent = new Intent(MainActivity.this, Signup.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(MainActivity.this, "Error!"+error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                        finish();

                    } else {
                        Intent intent = new Intent(MainActivity.this, Signup.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timer.start();

    }
}