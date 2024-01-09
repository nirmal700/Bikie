package com.bikie.in.Singup_Login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bikie.in.Dashboard;
import com.bikie.in.POJO_Classes.UserData;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bikie.in.Users.UserDashboard;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SignupProfilePicture extends AppCompatActivity {

    private Button mProceed;
    private ImageView mProfilePicture;
    private Uri mProfilePictureFilepath;
    private Boolean isProfilePictureUploaded = false;
    private final int STORAGE_PERMISSION_CODE = 1;
    private StorageReference storageReference;
    private String name, password, phoneNumber, gender, aadharNo, aadharFrontURL, aadharBackURL,mailId,dlNo,dlImageUrl;
    SessionManager manager;
    private ProgressDialog progressDialog;
    private boolean isMcWogChecked;
    private boolean isMcWgChecked;
    private boolean isLmvChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_picture);

        mProceed = findViewById(R.id.btn_Proceed);
        mProfilePicture = findViewById(R.id.img_ProfilePicture);
        progressDialog = new ProgressDialog(SignupProfilePicture.this);
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
        mailId = getIntent().getStringExtra("mailId");
        dlNo = getIntent().getStringExtra("dlNo");
        dlImageUrl = getIntent().getStringExtra("drivingLicenseURL");
        isLmvChecked = getIntent().getBooleanExtra("isMcWogChecked",false);
        isMcWgChecked = getIntent().getBooleanExtra("isMcWgChecked",false);
        isLmvChecked = getIntent().getBooleanExtra("isLmvChecked",false);

        if (!(ContextCompat.checkSelfPermission(SignupProfilePicture.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
        }
        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseProfilePicture();
            }
        });
        mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidProfilePicture()){
                    return;
                }else {
                    progressDialog.show();
                    StoreNewUserDataAndUpload();
                }
            }
        });
    }

    private void StoreNewUserDataAndUpload() {

        StorageReference customerDocsRef = storageReference.child("Customer_Documents/Profile_Picture/");
        StorageReference fileRef = customerDocsRef.child("PP_" + name+phoneNumber+".jpg");
        UploadTask uploadTask = fileRef.putFile(mProfilePictureFilepath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String mProfilePicURL = uri.toString();

                        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference reference = rootNode.getReference("Users");

                        UserData addNewUser = new UserData(name,phoneNumber,hashPassword(password),gender,aadharNo,aadharFrontURL,aadharBackURL,dlImageUrl,dlNo,isMcWogChecked,isMcWgChecked,isLmvChecked,mProfilePicURL,mailId);
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
                                String _profilePic = snapshot.child(phoneNumber).child("Profile").child("profilePictureURL").getValue(String.class);
                                String _mailId = snapshot.child(phoneNumber).child("Profile").child("mailID").getValue(String.class);
                                manager.setUserLogin(true);
                                manager.setDetails(_phoneNo, password,_name,_dlNo,_aadharNo,_dlIMGURL,_aadharFront,_aadharBack,_mailId,_profilePic); //Add Data To User Session manager

                                progressDialog.dismiss();

                                startActivity(new Intent(getApplicationContext(), UserDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                                Toast.makeText(SignupProfilePicture.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignupProfilePicture.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupProfilePicture.this, "Failure!" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean ValidProfilePicture() {
        if (isProfilePictureUploaded && mProfilePictureFilepath.toString().trim().length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Upload the image of Profile Picture", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void ChooseProfilePicture() {
        ImagePicker.Companion.with(SignupProfilePicture.this)
                .crop(3f,2f)
                .compress(200)
                .start(1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()
                != null) {
            mProfilePictureFilepath = data.getData();
            mProfilePicture.setImageURI(mProfilePictureFilepath);
            isProfilePictureUploaded = true;
        }

    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to show the gallery contents.")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(SignupProfilePicture.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}