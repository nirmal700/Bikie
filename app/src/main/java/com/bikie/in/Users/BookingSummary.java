package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bikie.in.BuildConfig;
import com.bikie.in.POJO_Classes.BookingConfirmation;
import com.bikie.in.R;
import com.bikie.in.SessionManager.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import kotlin.text.Charsets;

public class BookingSummary extends AppCompatActivity {

    Button mProceedToPayment;
    String apiEndPoint = "/pg/v1/pay";
    String merchantID = "PGTESTPAYUAT";
    String mCallBackURL ="https://webhook.site/3891c569-8b9c-45db-9946-62adc780307c";
    private static final String BASE_URL = "https://api-preprod.phonepe.com/apis/pg-sandbox/pg/v1/status/";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String VERIFY_HEADER = "X-VERIFY";
    private static final String MERCHANT_ID_HEADER = "X-MERCHANT-ID";
    private static int B2B_PG_REQUEST_CODE = 777;
    private static final String PREFIX = "BIKIE";
    private static final int RANDOM_STRING_LENGTH = 15;
    String base64String;
    String checksum;
    private MaterialCheckBox mTermsAgreed;
    private MaterialCheckBox mProduceValidDoc;
    private Button mProceed;
    double subtotal;
    private ImageView mVehicleImage;
    String merchantTransactionID;
    String vehicleID,vehicleImage,vehicleName,location,pickupTime,dropOffTime;
    Timestamp mPickupTimeStamp,mDropoffTimeStamp;
    Double helmetRental,priceToBook;
    private TextView mPickupDate, mPickupTime, mDropoffDate, mDropoffTime, mVehicleRental, mHelmetRental, mTotalRental, mLocation, mSubTotal;

    private Timestamp mBookingStartedTime = Timestamp.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        SessionManager manager = new SessionManager(BookingSummary.this);

        merchantTransactionID = generateRandomTransactionId();


        initializeViews();
        initializePhonePe();
        setupBookingDetails();
        setOnClickListenerForPayment();


    }

    private void setupBookingDetails() {
        vehicleID = getIntent().getStringExtra("VehicleID");
         priceToBook = getIntent().getDoubleExtra("CalculatedPrice", 0.00);
         helmetRental = getIntent().getDoubleExtra("HelmetCharges", 0.00);
         vehicleImage = getIntent().getStringExtra("VehicleImageURL");
         vehicleName = getIntent().getStringExtra("VehicleName");
         pickupTime = getIntent().getStringExtra("mPickupTimeStamp");
         dropOffTime = getIntent().getStringExtra("mDropoffTimeStamp");
         location = getIntent().getStringExtra("mVehicleLocation");

        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);

        Date pickupDateTime = parseDateTime(pickupTime, dateTimeFormat);
        Date dropoffDateTime = parseDateTime(dropOffTime, dateTimeFormat);

        mPickupDate.setText(displayDateFormat.format(pickupDateTime));
        mPickupTime.setText(displayTimeFormat.format(pickupDateTime));
        mDropoffDate.setText(displayDateFormat.format(dropoffDateTime));
        mDropoffTime.setText(displayTimeFormat.format(dropoffDateTime));
        mLocation.setText(location);
        mHelmetRental.setText(String.format(Locale.US, "₹ %.2f", helmetRental));
        mVehicleRental.setText(String.format(Locale.US, "₹ %.2f", priceToBook));
        subtotal = priceToBook + helmetRental;
        mSubTotal.setText(String.format(Locale.US, "₹ %.2f", subtotal));
        mTotalRental.setText(String.format(Locale.US, "₹ %.2f", subtotal));

        loadImageWithGlide(vehicleImage);
        prepareBase64AndChecksum();
    }

    private void prepareBase64AndChecksum() {
        Map<String, Object> merchantRequestMap = createMerchantRequestMap();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(merchantRequestMap).getAsJsonObject();
        String jsonString = jsonObject.toString();
        base64String = Base64.getEncoder().encodeToString(jsonString.getBytes(Charsets.UTF_8));

        try {
            checksum = convertToSHA256(base64String + apiEndPoint + BuildConfig.PHONEPE_UAT_SALT) + "###1";
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(BookingSummary.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        Log.e("Phonepe", "Checksum: "+checksum );
        Log.e("Phonepe", "BASE64: "+base64String );
    }

    private Map<String, Object> createMerchantRequestMap() {
        int mPhonepeTotal = (int) (subtotal*100);
        Map<String, Object> merchantRequestMap = new HashMap<>();
        merchantRequestMap.put("merchantId", merchantID);
        merchantRequestMap.put("merchantTransactionId", merchantTransactionID);
        merchantRequestMap.put("merchantUserId", "MUID676776123");
        merchantRequestMap.put("amount", mPhonepeTotal);
        merchantRequestMap.put("callbackUrl",mCallBackURL );
        merchantRequestMap.put("mobileNumber", "9999999999");

        // Create the nested paymentInstrument map
        Map<String, Object> paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMap.put("type", "PAY_PAGE");

        merchantRequestMap.put("paymentInstrument", paymentInstrumentMap);
        Log.e("Phonepe", "Merchant Request Map "+merchantRequestMap );
        Log.e("Phonepe", "Salt "+BuildConfig.PHONEPE_UAT_SALT );
        return merchantRequestMap;
    }

    private void loadImageWithGlide(String imageUrl) {
        Glide.with(BookingSummary.this)
                .load(imageUrl)
//                .placeholder(R.drawable.sand_clock)
                .into(mVehicleImage);
    }

    private Date parseDateTime(String dateTime, SimpleDateFormat format) {
        try {
            return format.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void setOnClickListenerForPayment() {
        mProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(BookingSummary.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.terms_and_conditions);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                mTermsAgreed = dialog.findViewById(R.id.checkbox_agree_terms);
                mProduceValidDoc = dialog.findViewById(R.id.checkbox_agree_license);
                mProceed = dialog.findViewById(R.id.button_proceed);

                mProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateSelection()) {
                            initiatePayment();
                        }
                    }
                });


            }
        });
    }

    private boolean validateSelection() {

        if (mTermsAgreed.isChecked() && mProduceValidDoc.isChecked()) {
            // Both checkboxes are checked, validation passed
            return true;
        } else {
            // Show error as not both checkboxes are checked
            Toast.makeText(this, "Please agree the Terms & Condition in order to proceed! .", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private void initiatePayment() {
        B2BPGRequest b2BPGRequest = createB2BPGRequest();
        try {
            startActivityForResult(PhonePe.getImplicitIntent(BookingSummary.this, b2BPGRequest, ""), B2B_PG_REQUEST_CODE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initializePhonePe() {
        PhonePe.init(BookingSummary.this, PhonePeEnvironment.UAT, "MERCHANTUAT", "");
    }

    private void initializeViews() {
        mPickupDate = findViewById(R.id.mPickupDate);
        mDropoffDate = findViewById(R.id.mDropoffDate);
        mPickupTime = findViewById(R.id.mPickupTime);
        mDropoffTime = findViewById(R.id.mDropoffTime);
        mLocation = findViewById(R.id.mLocation);
        mVehicleRental = findViewById(R.id.mVehicleRentalCharges);
        mHelmetRental = findViewById(R.id.mHelmetCharges);
        mTotalRental = findViewById(R.id.mTotalPayable);
        mSubTotal = findViewById(R.id.mSubTotal);
        mVehicleImage = findViewById(R.id.mBikeImage);
        mProceedToPayment = findViewById(R.id.btn_proceedToPayment);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) {

      /*This callback indicates only about completion of UI flow.
            Inform your server to make the transaction
            status call to get the status. Update your app with the
            success/failure status.*/
            CheckPaymentStatus();

        }
    }

    private B2BPGRequest createB2BPGRequest() {
        return new B2BPGRequestBuilder()
                .setData(base64String)
                .setChecksum(checksum)
                .setUrl(apiEndPoint)
                .build();
    }

    private void CheckPaymentStatus() {
        String url = BASE_URL + merchantID + "/" + merchantTransactionID;
        String mStatuschecksum = convertToSHA256("/pg/v1/status/" + merchantID + "/" + merchantTransactionID + BuildConfig.PHONEPE_UAT_SALT) + "###1";

        Log.e("Phonepe", "Checksum Status: " + mStatuschecksum);
        Log.e("Phonepe", "URL " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create headers
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER, "application/json");
        headers.put(VERIFY_HEADER, mStatuschecksum);
        headers.put(MERCHANT_ID_HEADER, merchantID);

        // Make a GET request using Volley
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.e("Phonepe", "Volley Response: " + response);
                        try {
                            if (response.has("success") && response.getBoolean("success"))
                            {
                                String transactionId = response.getJSONObject("data").getString("transactionId");
                                String merchantTransaction = response.getJSONObject("data").getString("merchantTransactionId");
                                String bankId = response.getJSONObject("data").getJSONObject("paymentInstrument").getString("bankId");
                                String bankTransactionId = response.getJSONObject("data").getJSONObject("paymentInstrument").getString("bankTransactionId");
                                String pgTransactionId = response.getJSONObject("data").getJSONObject("paymentInstrument").getString("pgTransactionId");
                                String type = response.getJSONObject("data").getJSONObject("paymentInstrument").getString("type");
                                String state = response.getJSONObject("data").getString("state");
                                mPickupTimeStamp = convertStringToTimestamp(pickupTime);
                                mDropoffTimeStamp = convertStringToTimestamp(dropOffTime);
                                Timestamp mCompletionTime = Timestamp.now();
                                String mBookingID = generateBookingId();
                                Log.e("Date", "onResponse: "+pickupTime+"Converted"+mPickupTimeStamp );

                                BookingConfirmation confirmation = new BookingConfirmation(String.format(Locale.US, "₹ %.2f", subtotal),merchantTransaction,bankId,bankTransactionId,pgTransactionId,type,transactionId,state,vehicleID,vehicleImage,vehicleName,location,true,true,mPickupTimeStamp,mDropoffTimeStamp,mBookingStartedTime,mCompletionTime,helmetRental,priceToBook,subtotal,mBookingID);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("UpcomingBookings")
                                        .document(mBookingID)
                                        .set(confirmation)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e("BookingSuccessFull", "onComplete: "+mBookingID );
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(BookingSummary.this, "Vehicle booking was not successful!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        // This block will be called if there is an error in the request
                        Log.e("Phonepe", "Volley Error: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        // Add the request to the request queue
        requestQueue.add(getRequest);

        Log.e("B2G", "CheckPaymentStatus: " + mStatuschecksum);
        Log.e("B2G", "CheckPaymentString: " + "/pg/v1/status/" + merchantID + "/" + merchantTransactionID + "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399");
    }


    public static String convertToSHA256(String input) {
        try {
            // Create a MessageDigest object for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update the digest with the input bytes
            md.update(input.getBytes());

            // Get the hash bytes
            byte[] digest = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your needs
            return null;
        }
    }
    public static String generateRandomTransactionId() {
        // Set the prefix for the transaction ID
        String prefix = "BI";

        // Generate random numbers for the transaction ID
        StringBuilder randomNumbers = new StringBuilder();
        Random random = new Random();

        // Adjust the length of the random numbers as needed
        int numberOfRandomDigits = 20;

        for (int i = 0; i < numberOfRandomDigits; i++) {
            randomNumbers.append(random.nextInt(10)); // Appending a random digit (0-9)
        }

        // Combine the prefix and random numbers to create the final transaction ID
        return prefix + randomNumbers.toString();
    }
    private static Timestamp convertStringToTimestamp(String dateString) {
        try {
            // Define your date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);

            // Parse the string to a Date object
            Date date = dateFormat.parse(dateString);

            // Convert Date to Timestamp
            return new Timestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parsing error as needed
        }
    }
    public static String generateBookingId() {
        StringBuilder bookingId = new StringBuilder(PREFIX);

        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            char randomChar = generateRandomChar();
            bookingId.append(randomChar);
        }

        return bookingId.toString();
    }

    private static char generateRandomChar() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(characters.length());
        return characters.charAt(randomIndex);
    }
}