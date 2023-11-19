package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.bikie.in.CustomTimePickerDialog;
import com.bikie.in.FeaturedAdapter;
import com.bikie.in.POJO_Classes.FeaturedBikes;
import com.bikie.in.POJO_Classes.FeaturedTestimonials;
import com.bikie.in.R;
import com.bikie.in.TestimonialAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView featuredBikes,featuredTestimonial;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter mTestimonialsadapter;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private TextInputEditText etPickupDate;
    private TextInputEditText etDropoffDate;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText etPickupTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        featuredBikes = findViewById(R.id.featured_recycler);
        featuredTestimonial = findViewById(R.id.testimonial_recycler);
        drawerLayout = findViewById(R.id.mDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        etPickupTime = findViewById(R.id.etPickupTime);

        etPickupDate = findViewById(R.id.etPickupDate);
        etDropoffDate = findViewById(R.id.etDropoffDate);
        disableEditText(etPickupDate);
        disableEditText(etDropoffDate);
        disableEditText(etPickupTime);
        setupDatePicker(etPickupDate);
        setupDatePicker(etDropoffDate);
        setupTimePicker(etPickupTime);


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(UserDashboard.this );

        featuredRecycler();
        testimonialRecycler();
    }
    private void setupDatePicker(final TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();

        // Listener for date picker dialog
        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Set the date to the calendar instance
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(editText, calendar);
        };

        // OnClickListener for the editText to open the DatePickerDialog
        editText.setOnClickListener(v -> {
            if (datePickerDialog == null || !datePickerDialog.isShowing()) {
                datePickerDialog = new DatePickerDialog(UserDashboard.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    private void updateLabel(TextInputEditText editText, Calendar calendar) {
        String myFormat = "dd MMM yyyy"; // Desired format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void testimonialRecycler() {
        featuredTestimonial.setHasFixedSize(true);
        featuredTestimonial.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<FeaturedTestimonials> testimonials = new ArrayList<>();
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_1.jpg?alt=media&token=239725d0-9bca-47f0-b0af-c30c8a980d72","RiderJake101","I've been using this app for a few months, and it's been a game changer for my daily commute. The bikes are always in great condition, and the rental process is super easy."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_2.jpg?alt=media&token=dd202b62-b7f1-4610-b0b7-206cc4f74036","SpeedySara22","Love the flexibility! Whether I need a quick ride across town or a leisurely cruise, I always find the perfect bike. The rates are very affordable too."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_3.jpg?alt=media&token=ef133b67-57ad-4311-8351-bb2a9f8202b2","EcoMoverEmma","As someone who's passionate about reducing my carbon footprint, this bike rental service has been a perfect fit. It's eco-friendly, convenient, and fun!"));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_4.jpg?alt=media&token=c77e0223-e456-46ef-8a60-79a9da6b8fda","UrbanExplorerLeo","Exploring the city has never been easier. I've discovered so many new places thanks to the accessibility of these bikes. The app is user-friendly and reliable."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_5.jpg?alt=media&token=9b6215d7-c919-4df2-be1a-d07464c16c0e","TwoWheelsTina","I rented a scooter for a weekend adventure, and it was an absolute blast. The scooters are fast, safe, and a great way to see the city. The app is user-friendly and reliable."));

        mTestimonialsadapter = new TestimonialAdapter(testimonials,UserDashboard.this);
        featuredTestimonial.setAdapter(mTestimonialsadapter);
    }

    private void featuredRecycler() {
        featuredBikes.setHasFixedSize(true);
        featuredBikes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<FeaturedBikes> bikes = new ArrayList<>();
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_apache.png?alt=media&token=a6685db0-b76d-4e98-b166-00a30d36582f","Apache RTR 150","Unleash the thrill with Apache RTR 160: Unmatched speed, sleek design, and a ride that defines excitement on every road."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_bmw.png?alt=media&token=3818ba3f-f701-4e5a-9cc4-97e293c0108a","BMW GS 310","Explore without limits on the BMW GS 310: A blend of robust power, adventure-ready design, and the spirit of the open road."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_fz.png?alt=media&token=85885aaa-e58e-4838-82a1-dce882edd55e","Fazer 150","Experience the rush with KTM: Cutting-edge technology, bold design, and a racing heritage that delivers exhilaration on every journey."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_gixer.png?alt=media&token=4b4a5da4-9aa1-4f12-988f-1837bb13fcce","Gixer 150","Feel the breeze, chase the horizon: The Fazer sets you free with its dynamic performance, sleek aesthetics, and the promise of adventure."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_ktm.png?alt=media&token=e62870a4-347c-4a3f-8c6d-212d484a4fec","KTM 350","Dominate the streets with the Gixxer: A fusion of aggressive styling, thrilling performance, and an adrenaline-pumping experience at every turn."));

        adapter = new FeaturedAdapter(UserDashboard.this,bikes);
        featuredBikes.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
    private void disableEditText(TextInputEditText editText) {
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackground(null);
    }
    private void setupTimePicker(final TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();

        // Listener for time picker dialog
        final TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            // Adjusting the minute to nearest 30 minutes interval
            if (minute < 15) {
                minute = 0;
            } else if (minute < 45) {
                minute = 30;
            } else {
                minute = 0;
                hourOfDay++;
            }

            // Update Calendar instance
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // Format and set time
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
            editText.setText(timeFormat.format(calendar.getTime()));
        };

        // OnClickListener for the editText to open the TimePickerDialog
        editText.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    UserDashboard.this,
                    timeSetListener,
                    hour,
                    minute,
                    false); // Use 'true' for 24-hour mode if needed
            timePickerDialog.show();
        });
    }

}