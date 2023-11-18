package com.bikie.in.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bikie.in.Dashboard;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Edit_Listed_Vehicle extends AppCompatActivity {

    private TextInputLayout mVehicleName, mVehicleNo, mVehicleInfo, mVehicleLocation, mVehicleTopSpeed, mVehicleMileage, mVehicleRent1Hr, mVehicleRent2Hr, mVehicleRent4Hr, mVehicleRent12Hr, mVehicleRent24Hr, mVehicleCC;
    private RadioGroup radioGroup;
    private RadioButton rb_bike, rb_scooty, rb_selected;
    private ProgressDialog progressDialog;
    private Button btn_mUpdateVehicle, btn_mDeleteVehicle;
    private ImageView btn_back, btn_ChooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_vehicle);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> Log.e("FireBase Anonymous Login ", "onSuccess: Anonymous Sign in Success")).addOnFailureListener(this, exception -> Log.e("FireBase Anonymous Login", "signInAnonymously:FAILURE", exception));
        }
        btn_back = findViewById(R.id.btn_backToSd);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        radioGroup = findViewById(R.id.radio_group);
        rb_bike = findViewById(R.id.radio_bike);
        rb_scooty = findViewById(R.id.radio_scooty);

        mVehicleName = findViewById(R.id.et_vName);
        mVehicleNo = findViewById(R.id.et_vNo);
        mVehicleInfo = findViewById(R.id.et_vInfo);
        mVehicleLocation = findViewById(R.id.et_vLocation);
        mVehicleTopSpeed = findViewById(R.id.et_vTopSpeed);
        mVehicleMileage = findViewById(R.id.et_vMileage);
        mVehicleRent1Hr = findViewById(R.id.et_v1Hour);
        mVehicleRent2Hr = findViewById(R.id.et_v2Hour);
        mVehicleRent4Hr = findViewById(R.id.et_v4Hour);
        mVehicleRent12Hr = findViewById(R.id.et_v12Hour);
        mVehicleRent24Hr = findViewById(R.id.et_v24Hour);
        mVehicleCC = findViewById(R.id.et_vCC);
        btn_mUpdateVehicle = findViewById(R.id.btn_vAddVehicle);
        btn_mDeleteVehicle = findViewById(R.id.btn_cancel);
        rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
        btn_ChooseImage = findViewById(R.id.btn_chooseImage);

        progressDialog = new ProgressDialog(Edit_Listed_Vehicle.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.cancel();

        String vehicleID = getIntent().getStringExtra("VehicleID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert vehicleID != null;
        Toast.makeText(this, "" + vehicleID, Toast.LENGTH_SHORT).show();
        db.collection("Vehicles").document(vehicleID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    NewVehicle vehicle = documentSnapshot.toObject(NewVehicle.class);
                    Objects.requireNonNull(mVehicleName.getEditText()).setText(vehicle.getmVehicleName().toString());
                    Objects.requireNonNull(mVehicleNo.getEditText()).setText(vehicle.getmVehicleNo().toString());
                    Objects.requireNonNull(mVehicleInfo.getEditText()).setText(vehicle.getmVehicleInfo().toString());
                    Objects.requireNonNull(mVehicleTopSpeed.getEditText()).setText(Integer.toString(vehicle.getmVehicleTopSpeed()));
                    Objects.requireNonNull(mVehicleMileage.getEditText()).setText(Integer.toString(vehicle.getmVehicleMileage()));
                    Objects.requireNonNull(mVehicleCC.getEditText()).setText(Integer.toString(vehicle.getmVehicleCC()));
                    Objects.requireNonNull(mVehicleRent1Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent1Hr()));
                    Objects.requireNonNull(mVehicleRent2Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent2Hr()));
                    Objects.requireNonNull(mVehicleRent4Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent4Hr()));
                    Objects.requireNonNull(mVehicleRent12Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent12Hr()));
                    Objects.requireNonNull(mVehicleRent24Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent24Hr()));
                    Objects.requireNonNull(mVehicleLocation.getEditText()).setText(vehicle.getmVehicleLocation());
                    if (Objects.equals(vehicle.getmVehicleCategory(), "Bike")) {
                        rb_bike.setChecked(true);
                    } else {
                        rb_scooty.setChecked(true);
                    }
                    Glide.with(Edit_Listed_Vehicle.this).load(vehicle.getmVehicleImages().get(0)).transform(new RoundedCorners(10)).into(btn_ChooseImage);

                    btn_mUpdateVehicle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (validateNumericDigitData(mVehicleTopSpeed) && validateNumericDigitData(mVehicleMileage) && validateNumericDigitData(mVehicleRent1Hr) && validateNumericDigitData(mVehicleRent2Hr) && validateNumericDigitData(mVehicleRent4Hr) && validateNumericDigitData(mVehicleRent12Hr) && validateNumericDigitData(mVehicleRent24Hr) && validateNumericDigitData(mVehicleCC) && validateVehicleRegistration(mVehicleNo) && validateTextLength(mVehicleName) && validateTextLength(mVehicleInfo) && validateTextLength(mVehicleLocation) && validateCategory()) {

                                rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
                                String category = rb_selected.getText().toString();
                                int _vehicle_speed = Integer.parseInt(mVehicleTopSpeed.getEditText().getText().toString());
                                int _vehicle_mileage = Integer.parseInt(mVehicleMileage.getEditText().getText().toString());
                                int _vehicle_cc = Integer.parseInt(mVehicleCC.getEditText().getText().toString());
                                int _vehicle_1hr = Integer.parseInt(mVehicleRent1Hr.getEditText().getText().toString());
                                int _vehicle_2hr = Integer.parseInt(mVehicleRent2Hr.getEditText().getText().toString());
                                int _vehicle_4hr = Integer.parseInt(mVehicleRent4Hr.getEditText().getText().toString());
                                int _vehicle_12hr = Integer.parseInt(mVehicleRent12Hr.getEditText().getText().toString());
                                int _vehicle_24hr = Integer.parseInt(mVehicleRent24Hr.getEditText().getText().toString());


                                Map<String, Object> updates = new HashMap<>();
                                updates.put("mVehicleInfo", mVehicleInfo.getEditText().getText().toString());  // Replace 'fieldName' and 'newValue' with your actual data
                                updates.put("mVehicleLocation", mVehicleLocation.getEditText().getText().toString());
                                updates.put("mVehicleMileage", _vehicle_mileage);
                                updates.put("mVehicleName", mVehicleName.getEditText().getText().toString());
                                updates.put("mVehicleNo", mVehicleNo.getEditText().getText().toString());
                                updates.put("mVehicleRent12Hr", _vehicle_12hr);
                                updates.put("mVehicleRent1Hr", _vehicle_1hr);
                                updates.put("mVehicleRent2Hr", _vehicle_2hr);
                                updates.put("mVehicleRent4Hr", _vehicle_4hr);
                                updates.put("mVehicleRent24Hr", _vehicle_24hr);
                                updates.put("mVehicleCC", _vehicle_cc);
                                updates.put("mVehicleCategory", category);
                                updates.put("mVehicleTopSpeed", _vehicle_speed);


                                FirebaseFirestore db = FirebaseFirestore.getInstance();


                                db.collection("Vehicles").document(vehicleID).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Edit_Listed_Vehicle.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    });

                } else {
                    Toast.makeText(Edit_Listed_Vehicle.this, "The Vehicle Doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit_Listed_Vehicle.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateNumericDigitData(TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            String text = editText.getText().toString();
            if (!text.matches("\\d{2,4}")) {
                textInputLayout.setError("Please enter a valid number (2-4 digits)");
                return false;
            } else {
                textInputLayout.setError(null); // Clear error
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean validateVehicleRegistration(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText() == null) {
            return false; // EditText is not found in the layout
        }

        String registrationNo = textInputLayout.getEditText().getText().toString().trim();
        String regex = "^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$";

        if (!registrationNo.matches(regex)) {
            textInputLayout.setError("Invalid registration number");
            return false;
        } else {
            textInputLayout.setError(null); // Clear error
            return true;
        }
    }

    public boolean validateTextLength(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText() == null) {
            return false; // Return false as EditText is not found
        }

        String text = textInputLayout.getEditText().getText().toString();
        if (text.length() > 4) {
            textInputLayout.setError(null); // Clear any existing error
            return true;
        } else {
            textInputLayout.setError("Text must be more than 4 characters");
            return false;
        }
    }

    private boolean validateCategory() {

        return radioGroup.getCheckedRadioButtonId() != -1;
    }
}