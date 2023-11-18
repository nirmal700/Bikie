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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Listed_Vehicles extends AppCompatActivity implements ListedVehiclesAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ListedVehiclesAdapter listedVehiclesAdapter;
    private ArrayList<NewVehicle> list;

    private FirebaseFirestore db;
    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached = false;
    private boolean isLoading = false;

    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_vehicles);

        initializeViews();
        initializeFirestore();
        loadInitialData();
        setupScrollListener();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.rv_ListedVehicles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(Listed_Vehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        list = new ArrayList<>();
        listedVehiclesAdapter = new ListedVehiclesAdapter(Listed_Vehicles.this, list);
        recyclerView.setAdapter(listedVehiclesAdapter);
        listedVehiclesAdapter.setOnItemClickListener(Listed_Vehicles.this);

        btn_back = findViewById(R.id.btn_backToSd);
    }

    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void loadInitialData() {
        db.collection("Vehicles")
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(NewVehicle.class));


                        }
                        Log.e("Inititial Data: ", "loadInitialData: "+list.toString() );
                        listedVehiclesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                        int documentSnapshotSize = task.getResult().size();
                        if (documentSnapshotSize > 0) {
                            lastVisible = task.getResult().getDocuments().get(documentSnapshotSize - 1);
                        }
                        isLastItemReached = documentSnapshotSize < 5;
                    } else {
                        Toast.makeText(Listed_Vehicles.this, "Error fetching documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (totalItemCount <= lastVisibleItem + 1) {
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        if (!isLastItemReached && !isLoading) {
            isLoading = true; // Set loading to true

            db.collection("Vehicles")
                    .startAfter(lastVisible)
                    .limit(5)
                    .get()
                    .addOnCompleteListener(task -> {
                        isLoading = false; // Loading done
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(NewVehicle.class));
                            }
                            listedVehiclesAdapter.notifyDataSetChanged();
                            int documentSnapshotSize = task.getResult().size();
                            if (documentSnapshotSize > 0) {
                                lastVisible = task.getResult().getDocuments().get(documentSnapshotSize - 1);
                            }
                            isLastItemReached = documentSnapshotSize < 5;
                        } else {
                            Toast.makeText(Listed_Vehicles.this, "Error fetching more documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void onItemClick(int position) {

        NewVehicle vehicleData = list.get(position);


        String id = vehicleData.getmVehicleId();

        Intent intent = new Intent(Listed_Vehicles.this, Edit_Listed_Vehicle.class);
        intent.putExtra("VehicleID", id);
        startActivity(intent);

    }
}