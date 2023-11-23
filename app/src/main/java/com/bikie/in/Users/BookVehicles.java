package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookVehicles extends AppCompatActivity {

    String pickupDateTimeString, dropoffDateTimeString;
    Timestamp requestedpickupDateTimeStamp,requesteddropoffDateTimeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_vehicles);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);
        pickupDateTimeString = getIntent().getStringExtra("pickupDateTime");
        dropoffDateTimeString = getIntent().getStringExtra("dropoffDateTime");
        try {
            Date pickupDateTime = dateTimeFormat.parse(pickupDateTimeString);
            assert pickupDateTime != null;
            requestedpickupDateTimeStamp = new Timestamp(pickupDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            Date dropoffDateTime = dateTimeFormat.parse(dropoffDateTimeString);
            assert dropoffDateTime != null;
            requesteddropoffDateTimeStamp = new Timestamp(dropoffDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference vehiclesRef = db.collection("Vehicles");
        CollectionReference bookingsRef = db.collection("Bookings");


// First, get all available vehicles
        vehiclesRef.whereEqualTo("mIsAvailable", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> availableVehicleIds = new ArrayList<>();
                List<NewVehicle> availableVehicleData = new ArrayList<>();

                for (QueryDocumentSnapshot vehicleDocument : task.getResult()) {
                    boolean isInPending = vehicleDocument.getBoolean("mIsInPending");
                    Timestamp bookingAttemptedTime = vehicleDocument.getTimestamp("mBookingAttemptedTime");

                    // Check if in pending and time difference is more than 3 mins
                    if (isInPending && (Timestamp.now().getSeconds() - bookingAttemptedTime.getSeconds()) > 180) {
                        // Update the vehicle document
                        vehicleDocument.getReference().update("mBookingAttemptedTime", null, "mIsInPending", false);
                    }

                    availableVehicleIds.add(vehicleDocument.getId());
                    availableVehicleData.add(vehicleDocument.toObject(NewVehicle.class));
                }
                Log.e("Vehicles", "onCreate: "+availableVehicleIds.toString() );
                // Now, check bookings for each available vehicle
                for (String vehicleId : availableVehicleIds) {
                    Query query = bookingsRef.whereEqualTo("vehicleId", vehicleId)
                            .whereLessThan("pickupTime", requesteddropoffDateTimeStamp)
                            .whereGreaterThan("dropoffTime", requestedpickupDateTimeStamp);

                    query.get().addOnCompleteListener(bookingTask -> {
                        if (bookingTask.isSuccessful()) {
                            boolean isAvailable = true;
                            for (QueryDocumentSnapshot document : bookingTask.getResult()) {
                                Log.e("Overlap", "onCreate: "+document.getData().toString() );
                                isAvailable = false;
                                break;
                            }

                            if (isAvailable) {
                                // Vehicle is available
                                Log.e("mAvailaible", "onCreate: "+vehicleId );
                            } else {
                                // Vehicle is not available
                            }
                        } else {
                            Toast.makeText(this, "Error! in Bookings query", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Bookings Failure", "onFailure: "+e.getMessage().toString());
                            Toast.makeText(BookVehicles.this, "Error!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                // Handle the error in vehicles query
                Toast.makeText(this, "Error! in vehicle query", Toast.LENGTH_SHORT).show();
            }
        });

    }
}