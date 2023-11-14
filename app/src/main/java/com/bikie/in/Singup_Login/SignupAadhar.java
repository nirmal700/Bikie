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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bikie.in.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class SignupAadhar extends AppCompatActivity {
    private TextInputLayout et_aadharNo;

    private ImageView img_aadharFront, img_aadharBack;
    private Uri mAadharFrontFilePath, mAadharBackFilePath;
    private Boolean isAadharFrontSelected = false,isAadharBackSelected = false;
    private String name, password, phoneNumber, gender, aadharNo;

    private final int STORAGE_PERMISSION_CODE = 1;

    private Button btn_mProceed;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_aadhar);

        et_aadharNo = findViewById(R.id.et_mAadharNo);
        img_aadharBack = findViewById(R.id.img_Aadhar_Back);
        img_aadharFront = findViewById(R.id.img_Aadhar_Front);
        btn_mProceed = findViewById(R.id.btn_Proceed);
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(SignupAadhar.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.cancel();

        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        gender = getIntent().getStringExtra("gender");

        if (!(ContextCompat.checkSelfPermission(SignupAadhar.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
        }
        img_aadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAadhaarFront();
            }
        });
        img_aadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAadharBack();
            }
        });

        btn_mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAadharNumber() | !validateAadharFront() | !validateAadharBack()) {

                    return;
                }
                else {
                    progressDialog.show();
                    UploadImages();
                }

            }
        });

    }

    private void UploadImages() {
        StorageReference customerDocsRef = storageReference.child("Customer_Documents/Aadhar_Details/");
        StorageReference fileRefFront = customerDocsRef.child("ADF_" + et_aadharNo.getEditText().getText().toString() + ".jpg");
        UploadTask uploadTask = fileRefFront.putFile(mAadharFrontFilePath);
        StorageReference fileRefBack = customerDocsRef.child("ADB_" + et_aadharNo.getEditText().getText().toString() + ".jpg");
        UploadTask uploadTaskBack = fileRefBack.putFile(mAadharBackFilePath);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRefFront.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String mAadharFrontURL = uri.toString();
                        uploadTaskBack.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRefBack.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String mAadharBackURL = uri.toString();

                                        new Handler().postDelayed(() -> {

                                            Intent DLIntent = new Intent(SignupAadhar.this, SingupDL.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            DLIntent.putExtra("phoneNumber", phoneNumber);
                                            DLIntent.putExtra("name", name);
                                            DLIntent.putExtra("password", password);
                                            DLIntent.putExtra("gender", gender);
                                            DLIntent.putExtra("aadharNo",aadharNo);
                                            DLIntent.putExtra("aadharFrontURL",mAadharFrontURL);
                                            DLIntent.putExtra("aadharBackURL",mAadharBackURL);
                                            progressDialog.dismiss();
                                            startActivity(DLIntent);
                                            finish();
                                        }, 1);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupAadhar.this, "Failed!"+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(SignupAadhar.this, "Failed!"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignupAadhar.this, "Failed!"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupAadhar.this, "Failed!"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateAadharBack() {
        if(isAadharBackSelected && mAadharBackFilePath.toString().trim().length() > 0)
        {
            return true;
        }
       else {
            Toast.makeText(this, "Please Upload the backside image of Aadhar Card", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateAadharFront() {
        if(isAadharFrontSelected && mAadharFrontFilePath.toString().trim().length() > 0)
        {
            return true;
        }
        else {
            Toast.makeText(this, "Please Upload the frontside image of Aadhar Card", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateAadharNumber() {
        aadharNo= Objects.requireNonNull(et_aadharNo.getEditText()).getText().toString().trim();

        if (aadharNo.isEmpty()) {
            et_aadharNo.setError("Field can not be empty");
            return false;
        } else if (aadharNo.length() > 12 | aadharNo.length() < 12) {
            et_aadharNo.setError("Please Enter 12 Digit Aadhar Number");
            return false;
        } else if (!aadharNo.matches("\\w*")) {
            et_aadharNo.setError("White spaces not allowed");
            return false;
        } else {
            et_aadharNo.setError(null);
            return true;
        }
    }

    private void ChooseAadharBack() {
        ImagePicker.Companion.with(SignupAadhar.this)
                .crop(3f,2f)
                .compress(200)
                .start(2);
    }

    private void ChooseAadhaarFront() {
        ImagePicker.Companion.with(SignupAadhar.this)
                .crop(3f,2f)
                .compress(200)
                .start(1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()
                != null) {
            mAadharFrontFilePath = data.getData();
            img_aadharFront.setImageURI(mAadharFrontFilePath);
            isAadharFrontSelected = true;
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData()
                != null) {
            mAadharBackFilePath = data.getData();
            img_aadharBack.setImageURI(mAadharBackFilePath);
            isAadharBackSelected = true;
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to show the gallery contents.")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(SignupAadhar.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
}