package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bikie.in.CustomTimePickerDialog;
import com.bikie.in.FeaturedAdapter;
import com.bikie.in.POJO_Classes.FeaturedBikes;
import com.bikie.in.POJO_Classes.FeaturedTestimonials;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bikie.in.Singup_Login.OtpVerification;
import com.bikie.in.Singup_Login.Signup;
import com.bikie.in.TestimonialAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView featuredBikes, featuredTestimonial;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter mTestimonialsadapter;


    static final float END_SCALE = 0.7f;
    LinearLayout contentView;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private TextInputEditText etPickupDate;
    private TextInputEditText etDropoffDate;
    private DatePickerDialog datePickerDialog;
    private TextInputEditText etPickupTime, etDropoffTime;
    private Button btn_mSearch;

    String pickupDateTimeString, dropoffDateTimeString;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        featuredBikes = findViewById(R.id.featured_recycler);
        featuredTestimonial = findViewById(R.id.testimonial_recycler);
        drawerLayout = findViewById(R.id.mDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        etPickupTime = findViewById(R.id.etPickupTime);
        etDropoffTime = findViewById(R.id.etDropoffTime);
        btn_mSearch = findViewById(R.id.btn_Search);

        etPickupDate = findViewById(R.id.etPickupDate);
        etDropoffDate = findViewById(R.id.etDropoffDate);
        contentView = findViewById(R.id.linear_content);
        disableEditText(etPickupDate);
        disableEditText(etDropoffDate);
        disableEditText(etPickupTime);
        disableEditText(etDropoffTime);

        setupDatePicker(etPickupDate);
        setupDatePicker(etDropoffDate);
        setupTimePicker(etPickupTime);
        setupTimePicker(etDropoffTime);


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(UserDashboard.this);

        btn_mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateTimesAndSearch()) {


                    Intent intent = new Intent(UserDashboard.this, BookVehicles.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("pickupDateTime", pickupDateTimeString);
                    intent.putExtra("dropoffDateTime", dropoffDateTimeString);
                    startActivity(intent);
                    finish();

                }
            }
        });

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
        featuredTestimonial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedTestimonials> testimonials = new ArrayList<>();
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_1.jpg?alt=media&token=239725d0-9bca-47f0-b0af-c30c8a980d72", "RiderJake101", "I've been using this app for a few months, and it's been a game changer for my daily commute. The bikes are always in great condition, and the rental process is super easy."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_2.jpg?alt=media&token=dd202b62-b7f1-4610-b0b7-206cc4f74036", "SpeedySara22", "Love the flexibility! Whether I need a quick ride across town or a leisurely cruise, I always find the perfect bike. The rates are very affordable too."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_3.jpg?alt=media&token=ef133b67-57ad-4311-8351-bb2a9f8202b2", "EcoMoverEmma", "As someone who's passionate about reducing my carbon footprint, this bike rental service has been a perfect fit. It's eco-friendly, convenient, and fun!"));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_4.jpg?alt=media&token=c77e0223-e456-46ef-8a60-79a9da6b8fda", "UrbanExplorerLeo", "Exploring the city has never been easier. I've discovered so many new places thanks to the accessibility of these bikes. The app is user-friendly and reliable."));
        testimonials.add(new FeaturedTestimonials("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Testimonials%2FTestimonial_5.jpg?alt=media&token=9b6215d7-c919-4df2-be1a-d07464c16c0e", "TwoWheelsTina", "I rented a scooter for a weekend adventure, and it was an absolute blast. The scooters are fast, safe, and a great way to see the city. The app is user-friendly and reliable."));

        mTestimonialsadapter = new TestimonialAdapter(testimonials, UserDashboard.this);
        featuredTestimonial.setAdapter(mTestimonialsadapter);
    }

    private void featuredRecycler() {
        featuredBikes.setHasFixedSize(true);
        featuredBikes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedBikes> bikes = new ArrayList<>();
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_apache.png?alt=media&token=a6685db0-b76d-4e98-b166-00a30d36582f", "Apache RTR 150", "Unleash the thrill with Apache RTR 160: Unmatched speed, sleek design, and a ride that defines excitement on every road."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_bmw.png?alt=media&token=3818ba3f-f701-4e5a-9cc4-97e293c0108a", "BMW GS 310", "Explore without limits on the BMW GS 310: A blend of robust power, adventure-ready design, and the spirit of the open road."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_fz.png?alt=media&token=85885aaa-e58e-4838-82a1-dce882edd55e", "Fazer 150", "Experience the rush with KTM: Cutting-edge technology, bold design, and a racing heritage that delivers exhilaration on every journey."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_gixer.png?alt=media&token=4b4a5da4-9aa1-4f12-988f-1837bb13fcce", "Gixer 150", "Feel the breeze, chase the horizon: The Fazer sets you free with its dynamic performance, sleek aesthetics, and the promise of adventure."));
        bikes.add(new FeaturedBikes("https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/Featured_Vehicles%2Fic_ktm.png?alt=media&token=e62870a4-347c-4a3f-8c6d-212d484a4fec", "KTM 350", "Dominate the streets with the Gixxer: A fusion of aggressive styling, thrilling performance, and an adrenaline-pumping experience at every turn."));

        adapter = new FeaturedAdapter(UserDashboard.this, bikes);
        featuredBikes.setAdapter(adapter);
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

        navigationDrawer();
        LottieAnimationView lottieAnimationView = findViewById(R.id.drawer_btn);

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lottieAnimationView.playAnimation();
                lottieAnimationView.loop(true);

                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });


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

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.mProfile);

        animateNavigationDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //drawerLayout.setScrimColor(getResources().getColor(R.color.red));
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mProfile) {
            profileSection();
        } else if (id == R.id.mHome) {
            Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mTermsAndConditions){
            Toast.makeText(getApplicationContext(), "Terms & Conditions!", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            terms_Conditions();
        } else if (id == R.id.mPrivacyPolicy){
            Toast.makeText(getApplicationContext(), "Privacy Policy!", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            privacy_policy();
        } else if (id == R.id.mCancellationPolicy){
            Toast.makeText(getApplicationContext(), "Return & Refund Policy!", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            refund_policy();
        } else if(id == R.id.mLogout){
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {

        manager = new SessionManager(getApplicationContext());

        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set Title
        builder.setTitle("Log out");

        //set Message
        builder.setMessage("Are you sure to Log out ?");

        //positive YES button
        builder.setPositiveButton("YES", (dialog, which) -> {

            manager.setUserLogin(false);
            manager.setDetails("", "", "", "","","","","");

            //activity.finishAffinity();
            dialog.dismiss();

            //Finish Activity
            startActivity(new Intent(getApplicationContext(), Signup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

        //Negative NO button
        builder.setNegativeButton("NO", (dialog, which) -> {
            //Dismiss Dialog
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void refund_policy() {
        startActivity(new Intent(getApplicationContext(), ReturnRefundPolicy.class));
    }

    private void privacy_policy() {
        startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
    }

    private void animateTermsAndConditions() {
        // Assuming you have a View for your Terms & Conditions content
        View termsAndConditionsView = findViewById(R.id.terms_condition_layout);

        // Add your desired animation here, for example, fade in
        termsAndConditionsView.setAlpha(0f);
        termsAndConditionsView.animate().alpha(1f).setDuration(500).start();
    }

    private void terms_Conditions() {
        startActivity(new Intent(getApplicationContext(), TermsAndConditions.class));
    }

    private void profileSection() {
        startActivity(new Intent(getApplicationContext(), UsersProfilePage.class));
    }

    private boolean validateTimesAndSearch() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);
        try {
            pickupDateTimeString = etPickupDate.getText().toString() + " " + etPickupTime.getText().toString();
            dropoffDateTimeString = etDropoffDate.getText().toString() + " " + etDropoffTime.getText().toString();
            Date pickupDateTime = dateTimeFormat.parse(pickupDateTimeString);
            Date dropoffDateTime = dateTimeFormat.parse(dropoffDateTimeString);

            // Check if the drop-off is after the pickup
            if (dropoffDateTime.before(pickupDateTime) || dropoffDateTime.equals(pickupDateTime)) {
                Toast.makeText(this, "Drop-off time must be after pickup time.", Toast.LENGTH_LONG).show();
                return false;
            }


            // Check if the difference is at least 1 hour
            long difference = dropoffDateTime.getTime() - pickupDateTime.getTime();
            if (difference < 3600000) { // 3600000 milliseconds = 1 hour
                Toast.makeText(this, "The time difference between pickup and drop-off should be at least 1 hour.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date/time format.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


//    private boolean isTimeWithinRange(Date time) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(time);
//
//        Calendar start = Calendar.getInstance();
//        start.setTime(time);
//        start.set(Calendar.HOUR_OF_DAY, 8);
//        start.set(Calendar.MINUTE, 0);
//        start.set(Calendar.SECOND, 0);
//        start.set(Calendar.MILLISECOND, 0);
//
//        Calendar end = Calendar.getInstance();
//        end.setTime(time);
//        end.set(Calendar.HOUR_OF_DAY, 21); // 9 PM
//        end.set(Calendar.MINUTE, 0);
//        end.set(Calendar.SECOND, 0);
//        end.set(Calendar.MILLISECOND, 0);
//
//        // Reset the date part to be the same for start, end, and the input time
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        start.set(year, month, day);
//        end.set(year, month, day);
//
//        return !time.before(start.getTime()) && !time.after(end.getTime());
//    }


}