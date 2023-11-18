package com.bikie.in.Singup_Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bikie.in.Dashboard;
import com.bikie.in.POJO_Classes.UserData;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class SingupDL extends AppCompatActivity {

    private Button mProceed;
    private ImageView mDrivingLicense;
    private Uri mDrivingLicenseFilepath;
    private Boolean isDrivingLicenseUploaded = false;
    private TextInputLayout et_drivingLicenseNo;
    private final int STORAGE_PERMISSION_CODE = 1;
    private MaterialCheckBox mcWogCheckBox;
    private MaterialCheckBox mcWgCheckBox;
    private MaterialCheckBox lmvCheckBox;
    private StorageReference storageReference;

    private boolean isMcWogChecked;
    private boolean isMcWgChecked;
    private boolean isLmvChecked;
    private String name, password, phoneNumber, gender, aadharNo, aadharFrontURL, aadharBackURL;
    SessionManager manager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup_dl);

        mProceed = findViewById(R.id.btn_Proceed);
        mDrivingLicense = findViewById(R.id.img_DrivingLicense);
        et_drivingLicenseNo = findViewById(R.id.et_DrivingLicenseNo);
        mcWgCheckBox = findViewById(R.id.mc_wg);
        mcWogCheckBox = findViewById(R.id.mc_wog);
        lmvCheckBox = findViewById(R.id.lmv);
        progressDialog = new ProgressDialog(SingupDL.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.cancel();

        manager = new SessionManager(getApplicationContext());

        storageReference = FirebaseStorage.getInstance().getReference();

        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        gender = getIntent().getStringExtra("gender");
        aadharNo = getIntent().getStringExtra("aadharNo");
        aadharFrontURL = getIntent().getStringExtra("aadharFrontURL");
        aadharBackURL = getIntent().getStringExtra("aadharBackURL");


        if (!(ContextCompat.checkSelfPermission(SingupDL.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
        }
        mDrivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDrivingLicense();
            }
        });
        mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateDLNumber() | !isAnyCheckboxChecked() | !ValidateDrivingLicenseImage()) {

                    return;
                } else {
                    progressDialog.show();
                    StoreNewUserDataAndUpload();
                }
            }
        });
        // Check their states and store them in boolean variables
        isMcWogChecked = mcWogCheckBox.isChecked();
        isMcWgChecked = mcWgCheckBox.isChecked();
        isLmvChecked = lmvCheckBox.isChecked();

        // You can add listeners if you want to update these booleans when the checkboxes are clicked
        mcWogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMcWogChecked = isChecked;
            }
        });

        mcWgCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMcWgChecked = isChecked;
            }
        });

        lmvCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLmvChecked = isChecked;
            }
        });

    }

    private void StoreNewUserDataAndUpload() {

        StorageReference customerDocsRef = storageReference.child("Customer_Documents/Driving_License_Details/");
        StorageReference fileRef = customerDocsRef.child("DL_" + et_drivingLicenseNo.getEditText().getText().toString() + ".jpg");
        UploadTask uploadTask = fileRef.putFile(mDrivingLicenseFilepath);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String dlImageURL = uri.toString();
                        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference reference = rootNode.getReference("Users");


                        UserData addNewUser = new UserData(name,phoneNumber,hashPassword(password),gender,aadharNo,aadharFrontURL,aadharBackURL,dlImageURL,et_drivingLicenseNo.getEditText().getText().toString(),isMcWogChecked,isMcWgChecked,isLmvChecked);
                        reference.child(phoneNumber).child("Profile").setValue(addNewUser);
                        reference.child(phoneNumber).child("phoneNumber").setValue(phoneNumber);

                        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber);

                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String _phoneNo = snapshot.child(phoneNumber).child("Profile").child("phoneNumber").getValue(String.class);
                                String _password = snapshot.child(phoneNumber).child("Profile").child("password").getValue(String.class);
                                String _name = snapshot.child(phoneNumber).child("Profile").child("name").getValue(String.class);
                                String _aadharNo = snapshot.child(phoneNumber).child("Profile").child("aadharNo").getValue(String.class);
                                String _dlNo = snapshot.child(phoneNumber).child("Profile").child("dlNo").getValue(String.class);
                                String _dlIMGURL = snapshot.child(phoneNumber).child("Profile").child("dlImageURL").getValue(String.class);
                                String _aadharFront = snapshot.child(phoneNumber).child("Profile").child("aadharFrontURL").getValue(String.class);
                                String _aadharBack = snapshot.child(phoneNumber).child("Profile").child("aadharBackURL").getValue(String.class);
                                manager.setUserLogin(true);
                                manager.setDetails(_phoneNo, password,_name,_dlNo,_aadharNo,_dlIMGURL,_aadharFront,_aadharBack); //Add Data To User Session manager

                                progressDialog.dismiss();

                                startActivity(new Intent(getApplicationContext(), Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                                Toast.makeText(SingupDL.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SingupDL.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SingupDL.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateDLNumber() {
        String val = Objects.requireNonNull(et_drivingLicenseNo.getEditText()).getText().toString().trim();
        if (val.isEmpty()) {
            et_drivingLicenseNo.setError("Field can not be empty");
            return false;
        } else if (val.matches("^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$")) {
            et_drivingLicenseNo.setError(null);
            return true;
        } else {
            et_drivingLicenseNo.setError("Enter a valid Driving License");
            return false;
        }
    }

    private boolean ValidateDrivingLicenseImage() {
        if (isDrivingLicenseUploaded && mDrivingLicenseFilepath.toString().trim().length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Upload the image of Driving License", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void ChooseDrivingLicense() {
        ImagePicker.Companion.with(SingupDL.this)
                .crop(3f,2f)
                .compress(200)
                .start(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()
                != null) {
            mDrivingLicenseFilepath = data.getData();
            mDrivingLicense.setImageURI(mDrivingLicenseFilepath);
            isDrivingLicenseUploaded = true;
        }

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to show the gallery contents.")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(SingupDL.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private boolean isAnyCheckboxChecked() {
        boolean isChecked = mcWogCheckBox.isChecked() || mcWgCheckBox.isChecked() || lmvCheckBox.isChecked();
        if (isChecked) {
            return isChecked;
        } else {
            Toast.makeText(this, "Check the allowed classes of vehicle", Toast.LENGTH_SHORT).show();
            return isChecked;
        }
    }
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}