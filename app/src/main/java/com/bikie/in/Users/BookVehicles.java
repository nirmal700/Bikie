package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bikie.in.Admin.Adapter.ListedVehiclesAdapter;
import com.bikie.in.Admin.Edit_Listed_Vehicle;
import com.bikie.in.Admin.Listed_Vehicles;
import com.bikie.in.POJO_Classes.Booking;
import com.bikie.in.POJO_Classes.BookingConfirmation;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bikie.in.Users.Adapter.AvailaibleVehiclesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BookVehicles extends AppCompatActivity implements AvailaibleVehiclesAdapter.OnItemClickListener {

    String pickupDateTimeString, dropoffDateTimeString;
    Timestamp requestedpickupDateTimeStamp, requesteddropoffDateTimeStamp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private AvailaibleVehiclesAdapter availaibleVehiclesAdapter;

    List<String> unavailableVehicleIds = new ArrayList<>();
    List<NewVehicle> availableVehicleDataList = new ArrayList<>();
    List<NewVehicle> unavailableVehicleDataList = new ArrayList<>();

    ImageView btn_back;
    RecyclerView.LayoutManager layoutManager;
    private TextView mDropOffDate, mPickupDate, mDropOffTime, mPickupTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_vehicles);


        initializeViews();

        setupDateTime();

        fetchAvailableVehicles();


    }

    private void fetchAvailableVehicles() {
        CollectionReference vehiclesRef = db.collection("Vehicles");
        // First, get all available vehicles
        vehiclesRef.whereEqualTo("mIsAvailable", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> availableVehicleIds = new ArrayList<>();
                List<NewVehicle> availableVehicleData = new ArrayList<>();


                for (QueryDocumentSnapshot vehicleDocument : task.getResult()) {
                   handleAvailableVehicle(vehicleDocument,availableVehicleIds,availableVehicleData);
                }

                // Inside onCreate or relevant method
                checkVehicleAvailability(availableVehicleIds, requestedpickupDateTimeStamp, requesteddropoffDateTimeStamp, new AvailabilityCheckCallback() {
                    @Override
                    public void onCheckComplete() {

                        separateVehicles(availableVehicleData, unavailableVehicleIds, availableVehicleDataList, unavailableVehicleDataList);

                        // Now update the RecyclerView with the available vehicles
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                availaibleVehiclesAdapter = new AvailaibleVehiclesAdapter(BookVehicles.this, availableVehicleDataList, requestedpickupDateTimeStamp, requesteddropoffDateTimeStamp);
                                availaibleVehiclesAdapter.setOnItemClickListener(BookVehicles.this);
                                recyclerView.setAdapter(availaibleVehiclesAdapter);
                                progressDialog.dismiss();

                                availaibleVehiclesAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });


                // Now, check bookings for each available vehicle


            } else {
                // Handle the error in vehicles query
                Toast.makeText(this, "Error! in vehicle query", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleAvailableVehicle(QueryDocumentSnapshot vehicleDocument, List<String> availableVehicleIds, List<NewVehicle> availableVehicleData) {
        boolean isInPending = vehicleDocument.getBoolean("mIsInPending");
        Timestamp bookingAttemptedTime = vehicleDocument.getTimestamp("mBookingAttemptedTime");

        if (isInPending && (Timestamp.now().getSeconds() - Objects.requireNonNull(bookingAttemptedTime).getSeconds()) > 180) {
            vehicleDocument.getReference().update("mBookingAttemptedTime", null, "mIsInPending", false);
        }

        availableVehicleIds.add(vehicleDocument.getId());
        availableVehicleData.add(vehicleDocument.toObject(NewVehicle.class));
    }

    private void setupDateTime() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);
        // Format for date and time
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        pickupDateTimeString = getIntent().getStringExtra("pickupDateTime");
        dropoffDateTimeString = getIntent().getStringExtra("dropoffDateTime");
        try {
            Date pickupDateTime = dateTimeFormat.parse(pickupDateTimeString);
            assert pickupDateTime != null;
            mPickupDate.setText(displayDateFormat.format(pickupDateTime));
            mPickupTime.setText(displayTimeFormat.format(pickupDateTime));
            requestedpickupDateTimeStamp = new Timestamp(pickupDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            Date dropoffDateTime = dateTimeFormat.parse(dropoffDateTimeString);
            assert dropoffDateTime != null;
            mDropOffDate.setText(displayDateFormat.format(dropoffDateTime));
            mDropOffTime.setText(displayTimeFormat.format(dropoffDateTime));
            requesteddropoffDateTimeStamp = new Timestamp(dropoffDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.rv_availaibleVehicles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = recyclerView.getLayoutManager();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(BookVehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        mDropOffDate = findViewById(R.id.dateTextDrop);
        mDropOffTime = findViewById(R.id.timeTextDrop);
        mPickupDate = findViewById(R.id.dateTextPickup);
        mPickupTime = findViewById(R.id.timeTextPickup);

        btn_back = findViewById(R.id.btn_backToSd);
    }

    @Override
    public void onItemClick(int position) {
        NewVehicle vehicleData = availableVehicleDataList.get(position);

        String id = vehicleData.getmVehicleId();

        Intent intent = new Intent(this, VehicleInformationToBook.class);
        // Pass any other necessary data through intent
        intent.putExtra("VehicleID", id);
        startActivity(intent);
    }

    interface AvailabilityCheckCallback {
        void onCheckComplete();
    }

    public void checkVehicleAvailability(final List<String> availableVehicleIds, final Timestamp requestedPickupTime, final Timestamp requestedDropoffTime, final AvailabilityCheckCallback callback) {
        final AtomicInteger counter = new AtomicInteger(availableVehicleIds.size());

        for (String vehicleId : availableVehicleIds) {
            checkBookingOverlap(vehicleId, requestedPickupTime, requestedDropoffTime, new Runnable() {
                @Override
                public void run() {
                    if (counter.decrementAndGet() == 0) {
                        callback.onCheckComplete();
                    }
                }
            });
        }
    }

    private void checkBookingOverlap(String vehicleId, Timestamp requestedPickupTime, Timestamp requestedDropoffTime, final Runnable onCheckComplete) {
        Query upcomingBookingsQuery = db.collection("UpcomingBookings").whereEqualTo("mVehicleID", vehicleId).whereLessThanOrEqualTo("mPickupDate", requestedDropoffTime);

        Query ongoingBookingsQuery = db.collection("OngoingBookings").whereEqualTo("mVehicleID", vehicleId).whereLessThanOrEqualTo("mPickupDate", requestedDropoffTime);

        Task<QuerySnapshot> upcomingTask = upcomingBookingsQuery.get();
        Task<QuerySnapshot> ongoingTask = ongoingBookingsQuery.get();

        Tasks.whenAllSuccess(upcomingTask, ongoingTask).addOnSuccessListener(results -> {
            for (Object result : results) {
                QuerySnapshot snapshot = (QuerySnapshot) result;
                for (QueryDocumentSnapshot document : snapshot) {
                    BookingConfirmation booking = document.toObject(BookingConfirmation.class);
                    Log.e("Booking", "checkBookingOverlap: " + booking.getmDropOffDate().compareTo(requestedPickupTime) + "-----" + booking.getmVehicleName());
                    if (booking.getmDropOffDate().compareTo(requestedPickupTime) > 0) {
                        // Vehicle is not available
                        Log.e("Not Available", "Not Avalaible Vehicle ID: " + vehicleId);
                        unavailableVehicleIds.add(vehicleId);
                        return; // Exit as soon as an overlapping booking is found
                    }
                }
            }
            // Vehicle is available
            Log.e("Available", "Vehicle ID: " + vehicleId);
        }).addOnFailureListener(e -> {
            Log.e("Booking Check Failure", "Error: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        onCheckComplete.run();
    }

    public void separateVehicles(List<NewVehicle> allVehicles, List<String> unavailableVehicleIds, List<NewVehicle> availableVehicleData, List<NewVehicle> unavailableVehicleData) {

        for (NewVehicle vehicle : allVehicles) {
            if (unavailableVehicleIds.contains(vehicle.getmVehicleId())) {
                unavailableVehicleData.add(vehicle);
            } else {
                availableVehicleData.add(vehicle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BookVehicles.this, UserDashboard.class));
        finishAffinity();
        super.onBackPressed();
    }

}