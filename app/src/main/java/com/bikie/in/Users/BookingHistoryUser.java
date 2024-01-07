package com.bikie.in.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikie.in.Admin.Adapter.ListedVehiclesAdapter;
import com.bikie.in.Admin.Edit_Listed_Vehicle;
import com.bikie.in.Admin.Listed_Vehicles;
import com.bikie.in.POJO_Classes.BookingConfirmation;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bikie.in.Users.Adapter.BookingHistoryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class BookingHistoryUser extends AppCompatActivity implements BookingHistoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private BookingHistoryAdapter bookingHistoryAdapter;
    private ArrayList<BookingConfirmation> list;

    private FirebaseFirestore db;

    private SessionManager manager;

    ImageView btn_back;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_user);
        initializeViews();
        initializeFirestore();
        fetchDataFromFirestore();
    }



    private void fetchDataFromFirestore() {

        Query upcomingQuery = db.collection("UpcomingBookings")
                .whereEqualTo("mUserPhoneNo", manager.getPhone())
                .orderBy("mBookingAttemptedTime", Query.Direction.DESCENDING)
                .limit(5);

        Query ongoingQuery = db.collection("OngoingBookings")
                .whereEqualTo("mUserPhoneNo", manager.getPhone())
                .orderBy("mBookingAttemptedTime", Query.Direction.DESCENDING)
                .limit(5);

        Query completedQuery = db.collection("CompletedBookings")
                .whereEqualTo("mUserPhoneNo", manager.getPhone())
                .orderBy("mBookingAttemptedTime", Query.Direction.DESCENDING)
                .limit(5);

        Task<QuerySnapshot> upcomingTask = upcomingQuery.get();
        Task<QuerySnapshot> ongoingTask = ongoingQuery.get();
        Task<QuerySnapshot> completedTask = completedQuery.get();
        Task<Void> combinedTask = Tasks.whenAll(upcomingTask, ongoingTask, completedTask);

        combinedTask.addOnCompleteListener(task -> {

            // Check if all tasks were successful
            if (task.isSuccessful()) {
                // Handle results for UpcomingBookings
                handleQueryResult(upcomingTask.getResult());

                // Handle results for OngoingBookings
                handleQueryResult(ongoingTask.getResult());

                // Handle results for CompletedBookings
                handleQueryResult(completedTask.getResult());

                runOnUiThread(() -> {
                    bookingHistoryAdapter = new BookingHistoryAdapter(BookingHistoryUser.this, list);
                    recyclerView.setAdapter(bookingHistoryAdapter);
                    bookingHistoryAdapter.setOnItemClickListener(BookingHistoryUser.this);
                    bookingHistoryAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                });

            } else {
                runOnUiThread(() -> {
                    Toast.makeText(BookingHistoryUser.this, "Error fetching documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
            }

            progressDialog.dismiss();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookingHistoryUser.this, "Failure getting Bookings!"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.e("BookingHistory", "onFailure: "+e.getMessage().toString() );
            }
        });
    }

private void handleQueryResult(QuerySnapshot querySnapshot) {
    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
        list.add(document.toObject(BookingConfirmation.class));
    }
}


    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.rv_BookingHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = recyclerView.getLayoutManager();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(BookingHistoryUser.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        list = new ArrayList<>();
        bookingHistoryAdapter = new BookingHistoryAdapter(BookingHistoryUser.this, list);
        recyclerView.setAdapter(bookingHistoryAdapter);
        bookingHistoryAdapter.setOnItemClickListener(BookingHistoryUser.this);

        btn_back = findViewById(R.id.btn_backToSd);

        manager = new SessionManager(BookingHistoryUser.this);
    }

    @Override
    public void onItemClick(int position) {

    }
}