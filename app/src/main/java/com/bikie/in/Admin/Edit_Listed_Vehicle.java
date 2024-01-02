package com.bikie.in.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bikie.in.Dashboard;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edit_Listed_Vehicle extends AppCompatActivity {

    private TextInputLayout mVehicleName, mVehicleNo, mVehicleInfo, mVehicleLocation, mVehicleTopSpeed, mVehicleMileage, mVehicleRent1Hr, mVehicleRent3Hr, mVehicleRent6Hr, mVehicleRent12Hr, mVehicleRent24Hr, mVehicleCC;
    private RadioGroup radioGroup;
    private RadioButton rb_bike;
    private RadioButton rb_scooty;
    private ProgressDialog progressDialog;
    private ImageView btn_back, btn_ChooseImage;
    private StorageReference storageReference;

    private MaterialCardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_vehicle);

        initializeFirebase();
        initializeViews();
        loadVehicleData();


    }

    private void loadVehicleData() {
        String vehicleID = getIntent().getStringExtra("VehicleID");
        if (vehicleID == null) {
            Toast.makeText(this, "Vehicle ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Vehicles").document(vehicleID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                displayVehicleData(documentSnapshot.toObject(NewVehicle.class));
            } else {
                Toast.makeText(Edit_Listed_Vehicle.this, "The Vehicle Doesn't exist!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(Edit_Listed_Vehicle.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void displayVehicleData(NewVehicle vehicle) {
        Objects.requireNonNull(mVehicleName.getEditText()).setText(vehicle.getmVehicleName().toString());
        Objects.requireNonNull(mVehicleNo.getEditText()).setText(vehicle.getmVehicleNo().toString());
        Objects.requireNonNull(mVehicleInfo.getEditText()).setText(vehicle.getmVehicleInfo().toString());
        Objects.requireNonNull(mVehicleTopSpeed.getEditText()).setText(Integer.toString(vehicle.getmVehicleTopSpeed()));
        Objects.requireNonNull(mVehicleMileage.getEditText()).setText(Integer.toString(vehicle.getmVehicleMileage()));
        Objects.requireNonNull(mVehicleCC.getEditText()).setText(Integer.toString(vehicle.getmVehicleCC()));
        Objects.requireNonNull(mVehicleRent1Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent1Hr()));
        Objects.requireNonNull(mVehicleRent3Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent3Hr()));
        Objects.requireNonNull(mVehicleRent6Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent6Hr()));
        Objects.requireNonNull(mVehicleRent12Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent12Hr()));
        Objects.requireNonNull(mVehicleRent24Hr.getEditText()).setText(Integer.toString(vehicle.getmVehicleRent24Hr()));
        Objects.requireNonNull(mVehicleLocation.getEditText()).setText(vehicle.getmVehicleLocation());
        if (Objects.equals(vehicle.getmVehicleCategory(), "Bike")) {
            rb_bike.setChecked(true);
        } else {
            rb_scooty.setChecked(true);
        }
        Glide.with(Edit_Listed_Vehicle.this).load(vehicle.getmVehicleImages().get(0)).transform(new RoundedCorners(10)).into(btn_ChooseImage);


    }

    private void initializeViews() {

        btn_back = findViewById(R.id.btn_backToSd);
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
        mVehicleRent3Hr = findViewById(R.id.et_v3Hour);
        mVehicleRent6Hr = findViewById(R.id.et_v6Hour);
        mVehicleRent12Hr = findViewById(R.id.et_v12Hour);
        mVehicleRent24Hr = findViewById(R.id.et_v24Hour);
        mVehicleCC = findViewById(R.id.et_vCC);
        Button btn_mUpdateVehicle = findViewById(R.id.btn_vAddVehicle);
        Button btn_mDeleteVehicle = findViewById(R.id.btn_cancel);
        RadioButton rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
        btn_ChooseImage = findViewById(R.id.btn_chooseImage);
        cardView = findViewById(R.id.mEditCard);
        String transitionName = getIntent().getStringExtra("EXTRA_TRANSITION_NAME");
        cardView.setTransitionName(transitionName);
        progressDialog = createProgressDialog();

        btn_mUpdateVehicle.setOnClickListener(v -> updateVehicleData());

    }

    private void updateVehicleData() {
        if (!validateInputs()) {
            return;
        }
        progressDialog.show();
        String category = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        int _vehicle_speed = Integer.parseInt(Objects.requireNonNull(mVehicleTopSpeed.getEditText()).getText().toString());
        int _vehicle_mileage = Integer.parseInt(Objects.requireNonNull(mVehicleMileage.getEditText()).getText().toString());
        int _vehicle_cc = Integer.parseInt(Objects.requireNonNull(mVehicleCC.getEditText()).getText().toString());
        int _vehicle_1hr = Integer.parseInt(Objects.requireNonNull(mVehicleRent1Hr.getEditText()).getText().toString());
        int _vehicle_3hr = Integer.parseInt(Objects.requireNonNull(mVehicleRent3Hr.getEditText()).getText().toString());
        int _vehicle_6hr = Integer.parseInt(Objects.requireNonNull(mVehicleRent6Hr.getEditText()).getText().toString());
        int _vehicle_12hr = Integer.parseInt(Objects.requireNonNull(mVehicleRent12Hr.getEditText()).getText().toString());
        int _vehicle_24hr = Integer.parseInt(Objects.requireNonNull(mVehicleRent24Hr.getEditText()).getText().toString());

        Map<String, Object> updates = new HashMap<>();
        updates.put("mVehicleInfo", Objects.requireNonNull(mVehicleInfo.getEditText()).getText().toString());  // Replace 'fieldName' and 'newValue' with your actual data
        updates.put("mVehicleLocation", Objects.requireNonNull(mVehicleLocation.getEditText()).getText().toString());
        updates.put("mVehicleMileage", _vehicle_mileage);
        updates.put("mVehicleName", Objects.requireNonNull(mVehicleName.getEditText()).getText().toString());
        updates.put("mVehicleNo", Objects.requireNonNull(mVehicleNo.getEditText()).getText().toString());
        updates.put("mVehicleRent12Hr", _vehicle_12hr);
        updates.put("mVehicleRent1Hr", _vehicle_1hr);
        updates.put("mVehicleRent3Hr", _vehicle_3hr);
        updates.put("mVehicleRent6Hr", _vehicle_6hr);
        updates.put("mVehicleRent24Hr", _vehicle_24hr);
        updates.put("mVehicleCC", _vehicle_cc);
        updates.put("mVehicleCategory", category);
        updates.put("mVehicleTopSpeed", _vehicle_speed);

        String vehicleID = getIntent().getStringExtra("VehicleID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Vehicles").document(Objects.requireNonNull(vehicleID)).update(updates)
                .addOnSuccessListener(unused -> {
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e ->
                        Toast.makeText(Edit_Listed_Vehicle.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        progressDialog.dismiss();
    }


    private boolean validateInputs() {
        return validateNumericDigitData(mVehicleTopSpeed) && validateNumericDigitData(mVehicleMileage) && validateNumericDigitData(mVehicleRent1Hr) && validateNumericDigitData(mVehicleRent3Hr) && validateNumericDigitData(mVehicleRent6Hr) && validateNumericDigitData(mVehicleRent12Hr) && validateNumericDigitData(mVehicleRent24Hr) && validateNumericDigitData(mVehicleCC) && validateVehicleRegistration(mVehicleNo) && validateTextLength(mVehicleName) && validateTextLength(mVehicleInfo) && validateTextLength(mVehicleLocation) && validateCategory();
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(Edit_Listed_Vehicle.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void initializeFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously().addOnSuccessListener(this, unused ->
                            Log.e("Firebase Login", "Anonymous Sign in Success"))
                    .addOnFailureListener(this, e ->
                            Log.e("Firebase Login", "signInAnonymously:FAILURE", e));
        }
        // Consider storing this reference as a class variable if used elsewhere
        storageReference = FirebaseStorage.getInstance().getReference();

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