package com.bikie.in.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddNewVehicle extends AppCompatActivity {

    private TextInputLayout mVehicleName, mVehicleNo, mVehicleInfo, mVehicleLocation, mVehicleTopSpeed, mVehicleMileage, mVehicleRent1Hr, mVehicleRent3Hr, mVehicleRent6Hr, mVehicleRent12Hr, mVehicleRent24Hr, mVehicleCC;
    private RadioGroup radioGroup;
    private RadioButton rb_selected;
    private ProgressDialog progressDialog;
    private Button btn_mAddVehicle, btn_cancel;
    private Uri mVehicleUri;

    //vars
    private DatabaseReference root;
    private ImageView btn_back, btn_ChooseImage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);

        initializeFirebase();
        initializeViews();

        progressDialog = createProgressDialog();


    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(AddNewVehicle.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void addNewVehicle() {
        if (validateInputs()) {
            progressDialog.show();
            String category = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
            StorageReference vehicleImageRef = storageReference.child("Vehicle_Images/");
            StorageReference fileRef = vehicleImageRef.child("VI_" + UUID.randomUUID() + ".jpg");
            UploadTask uploadTask = fileRef.putFile(mVehicleUri);
            rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
            int _vehicle_speed = Integer.parseInt(mVehicleTopSpeed.getEditText().getText().toString());
            int _vehicle_mileage = Integer.parseInt(mVehicleMileage.getEditText().getText().toString());
            int _vehicle_cc = Integer.parseInt(mVehicleCC.getEditText().getText().toString());
            int _vehicle_1hr = Integer.parseInt(mVehicleRent1Hr.getEditText().getText().toString());
            int _vehicle_3hr = Integer.parseInt(mVehicleRent3Hr.getEditText().getText().toString());
            int _vehicle_6hr = Integer.parseInt(mVehicleRent6Hr.getEditText().getText().toString());
            int _vehicle_12hr = Integer.parseInt(mVehicleRent12Hr.getEditText().getText().toString());
            int _vehicle_24hr = Integer.parseInt(mVehicleRent24Hr.getEditText().getText().toString());
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();
            GeoPoint location = new GeoPoint(20.469460, 85.900783);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ArrayList<String> urlList = new ArrayList<>();
                            String _bike_url = uri.toString();
                            urlList.add(_bike_url);
                            NewVehicle vehicle = new NewVehicle(uuidString, mVehicleName.getEditText().getText().toString(), mVehicleNo.getEditText().getText().toString(), mVehicleInfo.getEditText().getText().toString(), mVehicleLocation.getEditText().getText().toString(), category, location, _vehicle_speed, _vehicle_mileage, _vehicle_1hr, _vehicle_3hr, _vehicle_6hr, _vehicle_12hr, _vehicle_24hr, _vehicle_cc, false, true, null, urlList);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Vehicles").document(uuidString).set(vehicle)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddNewVehicle.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewVehicle.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewVehicle.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInputs() {
        return validateNumericDigitData(mVehicleTopSpeed) && validateNumericDigitData(mVehicleMileage) && validateNumericDigitData(mVehicleRent1Hr) && validateNumericDigitData(mVehicleRent3Hr) && validateNumericDigitData(mVehicleRent6Hr) && validateNumericDigitData(mVehicleRent12Hr) && validateNumericDigitData(mVehicleRent24Hr) && validateNumericDigitData(mVehicleCC) && validateVehicleRegistration(mVehicleNo) && validateTextLength(mVehicleName) && validateTextLength(mVehicleInfo) && validateTextLength(mVehicleLocation) && validateCategory();
    }

    private void initializeViews() {
        btn_back = findViewById(R.id.btn_backToSd);
        radioGroup = findViewById(R.id.radio_group);
        rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
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
        btn_mAddVehicle = findViewById(R.id.btn_vAddVehicle);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ChooseImage = findViewById(R.id.btn_chooseImage);

        btn_ChooseImage.setOnClickListener(v -> chooseImage());
        btn_mAddVehicle.setOnClickListener(v -> addNewVehicle());
    }

    private void initializeFirebase() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInAnonymously(mAuth);
        }
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void signInAnonymously(FirebaseAuth mAuth) {
        mAuth.signInAnonymously()
                .addOnSuccessListener(this, authResult -> Log.e("Firebase Login", "Anonymous Sign in Success"))
                .addOnFailureListener(this, e -> Log.e("Firebase Login", "signInAnonymously:FAILURE", e));
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//-- checking request code and result code if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK then set image in the image view--

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            mVehicleUri = data.getData();
            btn_ChooseImage.setImageURI(mVehicleUri);

        }

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