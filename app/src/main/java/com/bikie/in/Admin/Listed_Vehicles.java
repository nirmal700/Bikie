package com.bikie.in.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bikie.in.Admin.Adapter.ListedVehiclesAdapter;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Listed_Vehicles extends AppCompatActivity implements ListedVehiclesAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ListedVehiclesAdapter listedVehiclesAdapter;
    private ArrayList<NewVehicle> list;

    ImageView btn_back;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_vehicles);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> Log.e("FireBase Anonymous Login ", "onSuccess: Anonymous Sign in Success"))
                    .addOnFailureListener(this, exception -> Log.e("FireBase Anonymous Login", "signInAnonymously:FAILURE", exception));
        }

        btn_back = findViewById(R.id.btn_backToSd);
        recyclerView = findViewById(R.id.rv_ListedVehicles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(Listed_Vehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        list = new ArrayList<>();

        listedVehiclesAdapter = new ListedVehiclesAdapter(Listed_Vehicles.this, list);

        recyclerView.setAdapter(listedVehiclesAdapter);

        listedVehiclesAdapter.setOnItemClickListener(Listed_Vehicles.this);

        db.collection("Vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        NewVehicle vehicleData = documentSnapshot.toObject(NewVehicle.class);
                        list.add(vehicleData);
                    }
                    recyclerView.setAdapter(listedVehiclesAdapter);
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Listed_Vehicles.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void onItemClick(int position) {

        NewVehicle vehicleData = list.get(position);


        String id = vehicleData.getmVehicleId();
        Log.e("Click", "onItemClick: "+id+""+vehicleData.toString() );

//        FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("id").equalTo(id)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        if (snapshot.exists()) {
//                            Intent intent = new Intent(UploadedVehicles.this, SingleUploadedVehicles.class);
//                            intent.putExtra("VehicleId", id); // Pass Shop Id value To ShopDetailsSingleView
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(UploadedVehicles.this, "Vehicle does not exist", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(UploadedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

    }
}