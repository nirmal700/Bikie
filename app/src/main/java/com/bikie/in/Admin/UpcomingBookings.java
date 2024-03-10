package com.bikie.in.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikie.in.Admin.Adapter.UpcomingBookingsAdapter;
import com.bikie.in.POJO_Classes.BookingConfirmation;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bikie.in.Users.Adapter.BookingHistoryAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpcomingBookings extends AppCompatActivity implements UpcomingBookingsAdapter.OnItemClickListener  {
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private UpcomingBookingsAdapter upcomingBookingsAdapter;
    private ArrayList<BookingConfirmation> list;

    private FirebaseFirestore db;


    ImageView btn_back;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_bookings);
        initializeViews();
        initializeFirestore();
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        Query upcomingQuery = db.collection("UpcomingBookings")
                .orderBy("mBookingAttemptedTime", Query.Direction.ASCENDING);

        upcomingQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (BookingConfirmation bookingConfirmation : querySnapshot.toObjects(BookingConfirmation.class)) {
                        list.add(bookingConfirmation);
                    }
                    upcomingBookingsAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            } else {
                Toast.makeText(UpcomingBookings.this, "Error fetching documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(UpcomingBookings.this, "Failure getting Bookings!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.rv_UpcomingBooking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = recyclerView.getLayoutManager();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(UpcomingBookings.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        list = new ArrayList<>();
        upcomingBookingsAdapter = new UpcomingBookingsAdapter(UpcomingBookings.this, list);
        recyclerView.setAdapter(upcomingBookingsAdapter);
        upcomingBookingsAdapter.setOnItemClickListener(UpcomingBookings.this);

        btn_back = findViewById(R.id.btn_backToSd);


    }

    @Override
    public void onItemClick(int position) {

    }
}