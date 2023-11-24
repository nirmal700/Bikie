package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bikie.in.Users.Adapter.VehicleViewImageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VehicleInformationToBook extends AppCompatActivity {

    private TextView mLocation,mVehicleName,mPrice,mVehicleInfo,mTopSpeed,mVehicleCC,mVehicleMileage;
    private Button mBookNow;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> arrayList = new ArrayList<>();
    private MaterialCheckBox checkBoxOneHelmet;
    private MaterialCheckBox checkBoxTwoHelmets;
    private Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information_to_book);

        mLocation = findViewById(R.id.mLocation);
        mVehicleName = findViewById(R.id.mVehicleName);
        mPrice = findViewById(R.id.mPrice);
        mVehicleInfo = findViewById(R.id.mVehicleDescription);
        mTopSpeed = findViewById(R.id.mTopSpeed);
        mVehicleMileage = findViewById(R.id.mVehicleMileage);
        mVehicleCC = findViewById(R.id.mVehicleCC);
        mBookNow = findViewById(R.id.btn_mBookNow);
        RecyclerView recyclerView = findViewById(R.id.recyclerVehicleImages);

        String vehicleID = getIntent().getStringExtra("VehicleID");
        DocumentReference docRef = db.collection("Vehicles").document(vehicleID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        NewVehicle vehicle = documentSnapshot.toObject(NewVehicle.class);
                        mLocation.setText(vehicle.getmVehicleLocation());
                        mVehicleName.setText(vehicle.getmVehicleName());
                        mPrice.setText(String.format("₹ %s", vehicle.getmVehicleRent1Hr()));
                        mVehicleInfo.setText(vehicle.getmVehicleInfo());
                        mTopSpeed.setText(String.format("%d Km/h", vehicle.getmVehicleTopSpeed()));
                        mVehicleMileage.setText(String.format("%d Km/L", vehicle.getmVehicleMileage()));
                        mVehicleCC.setText(String.format("%d CC", vehicle.getmVehicleCC()));
                        arrayList.addAll(vehicle.getmVehicleImages());
                        VehicleViewImageAdapter adapter = new VehicleViewImageAdapter(VehicleInformationToBook.this, arrayList);
                        recyclerView.setAdapter(adapter);

                        mBookNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(VehicleInformationToBook.this);

                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.add_on_helmet);

                                dialog.show();
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
                                dialog.getWindow().setGravity(Gravity.BOTTOM);

                                checkBoxOneHelmet = dialog.findViewById(R.id.checkbox_one_helmet);
                                checkBoxTwoHelmets = dialog.findViewById(R.id.checkbox_two_helmets);
                                buttonAdd = dialog.findViewById(R.id.button_add);

                                checkBoxOneHelmet.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        checkBoxTwoHelmets.setChecked(false);
                                       
                                    }
                                });

                                checkBoxTwoHelmets.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        checkBoxOneHelmet.setChecked(false);
                                        
                                    }
                                });
                                
                                buttonAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (validateSelection()) {
                                            Toast.makeText(VehicleInformationToBook.this, "Added Helmets", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            }
                        });

                        adapter.setOnItemClickListener(new VehicleViewImageAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(ImageView imageView, String path) {
                                startActivity(new Intent(VehicleInformationToBook.this, ImageViewVehicle.class).putExtra("image", path), ActivityOptions.makeSceneTransitionAnimation(VehicleInformationToBook.this, imageView, "image").toBundle());
                            }
                        });
                    }
                    else {
                        Toast.makeText(VehicleInformationToBook.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(VehicleInformationToBook.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VehicleInformationToBook.this, "Error!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private boolean validateSelection() {
        if (!checkBoxOneHelmet.isChecked() && !checkBoxTwoHelmets.isChecked()) {
            // Show error as no checkboxes are checked
            Toast.makeText(this, "Please select at least one option.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}