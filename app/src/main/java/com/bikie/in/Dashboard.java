package com.bikie.in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikie.in.Admin.AddNewVehicle;
import com.bikie.in.Admin.Listed_Vehicles;
import com.bikie.in.SessionManager.SessionManager;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
    MaterialCardView  btn_addNewVehicle,btn_listedVehicles;
    TextView tv_userName;

    SessionManager manager;
    String phoneNumber, currentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> Log.e("FireBase Anonymous Login ", "onSuccess: Anonymous Sign in Success"))
                    .addOnFailureListener(this, exception -> Log.e("FireBase Anonymous Login", "signInAnonymously:FAILURE", exception));
        }
        btn_addNewVehicle = findViewById(R.id.button_AddNewVehicle);
        btn_listedVehicles = findViewById(R.id.btn_ListedVehicles);

        tv_userName = findViewById(R.id.tv_userName);

        manager = new SessionManager(getApplicationContext());
        phoneNumber = manager.getPhone();
        currentName = manager.getName();

        tv_userName.setText("Hai, " + currentName);

        btn_addNewVehicle.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddNewVehicle.class)));
        btn_listedVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Listed_Vehicles.class));
            }
        });
    }
}