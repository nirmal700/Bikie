package com.bikie.in.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.bikie.in.HtmlToPdfAndSendEmailTask;
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
import java.util.Objects;
import java.util.Random;

import kotlin.text.Charsets;

public class BookingSummary extends AppCompatActivity {

    Button mProceedToPayment;
    String apiEndPoint = "/pg/v1/pay";
    String merchantID = "PGTESTPAYUAT";
    String mCallBackURL = "https://webhook.site/3891c569-8b9c-45db-9946-62adc780307c";
    private static final String BASE_URL = "https://api-preprod.phonepe.com/apis/pg-sandbox/pg/v1/status/";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String VERIFY_HEADER = "X-VERIFY";
    private static final String MERCHANT_ID_HEADER = "X-MERCHANT-ID";
    private static int B2B_PG_REQUEST_CODE = 777;
    private ProgressDialog progressDialog;
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
    String vehicleID, vehicleImage, vehicleName, location, pickupTime, dropOffTime;
    Timestamp mPickupTimeStamp, mDropoffTimeStamp;
    Double helmetRental, priceToBook;
    private TextView mPickupDate, mPickupTime, mDropoffDate, mDropoffTime, mVehicleRental, mHelmetRental, mTotalRental, mLocation, mSubTotal;

    private Timestamp mBookingStartedTime = Timestamp.now();
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        manager = new SessionManager(BookingSummary.this);

        merchantTransactionID = generateRandomTransactionId();


        initializeViews();
        initializePhonePe();
        setupBookingDetails();
        setOnClickListenerForPayment();


    }

    private void setupBookingDetails() {
        progressDialog.show();
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
        progressDialog.dismiss();
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
        Log.e("Phonepe", "Checksum: " + checksum);
        Log.e("Phonepe", "BASE64: " + base64String);
    }

    private Map<String, Object> createMerchantRequestMap() {
        int mPhonepeTotal = (int) (subtotal * 100);
        Map<String, Object> merchantRequestMap = new HashMap<>();
        merchantRequestMap.put("merchantId", merchantID);
        merchantRequestMap.put("merchantTransactionId", merchantTransactionID);
        merchantRequestMap.put("merchantUserId", "MUID676776123");
        merchantRequestMap.put("amount", mPhonepeTotal);
        merchantRequestMap.put("callbackUrl", mCallBackURL);
        merchantRequestMap.put("mobileNumber", "9999999999");

        // Create the nested paymentInstrument map
        Map<String, Object> paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMap.put("type", "PAY_PAGE");

        merchantRequestMap.put("paymentInstrument", paymentInstrumentMap);
        Log.e("Phonepe", "Merchant Request Map " + merchantRequestMap);
        Log.e("Phonepe", "Salt " + BuildConfig.PHONEPE_UAT_SALT);
        return merchantRequestMap;
    }

    private void loadImageWithGlide(String imageUrl) {
        Glide.with(BookingSummary.this).load(imageUrl)
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
        PhonePe.init(BookingSummary.this, PhonePeEnvironment.STAGE, "MERCHANTUAT", "");
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
        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(BookingSummary.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
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
        return new B2BPGRequestBuilder().setData(base64String).setChecksum(checksum).setUrl(apiEndPoint).build();
    }

    private void CheckPaymentStatus() {

        progressDialog.show();
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
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Handle the response
                Log.e("Phonepe", "Volley Response: " + response);
                try {
                    if (response.has("success") && response.getBoolean("success")) {
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
                        // Convert Timestamp to Date
                        Date date = mCompletionTime.toDate();
                        // Define the desired date format
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                        // Format the date
                        String currentTime = sdf.format(date);
                        String mBookingID = generateBookingId();

                        String invoiceHtml = generateInvoice(manager.getName(), manager.getPhone(), mBookingID, currentTime, "PHONEPE", merchantTransaction, vehicleName, pickupTime+ " To " +dropOffTime, String.format(Locale.US, "₹ %.2f", priceToBook), String.format(Locale.US, "₹ %.2f", helmetRental), "₹ 0", String.format(Locale.US, "₹ %.2f", subtotal), String.format(Locale.US, "₹ %.2f", subtotal));

                        HtmlToPdfAndSendEmailTask sendEmailTask = new HtmlToPdfAndSendEmailTask(BookingSummary.this, manager.getMailId());
                        sendEmailTask.execute(invoiceHtml);

                        BookingConfirmation confirmation = new BookingConfirmation(String.format(Locale.US, "₹ %.2f", subtotal), merchantTransaction, bankId, bankTransactionId, pgTransactionId, type, transactionId, state, vehicleID, vehicleImage, vehicleName, location, true, true, mPickupTimeStamp, mDropoffTimeStamp, mBookingStartedTime, mCompletionTime, String.format(Locale.US, "₹ %.2f", helmetRental), String.format(Locale.US, "₹ %.2f", priceToBook), String.format(Locale.US, "₹ %.2f", subtotal), mBookingID, manager.getName(), manager.getPhone().toString(), manager.getAadharNo().toString(), manager.getDlNo().toString(), manager.getAadharFrontURL().toString(), manager.getAadharBackURL().toString(), manager.getDlImageURL());
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("UpcomingBookings").document(mBookingID).set(confirmation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e("BookingSuccessFull", "onComplete: " + mBookingID);
                                Intent intent = new Intent(BookingSummary.this, BookingSuccessfullPage.class);
                                intent.putExtra("BookingID", mBookingID);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BookingSummary.this, "Vehicle booking was not successful!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                // This block will be called if there is an error in the request
                Log.e("Phonepe", "Volley Error: " + error.toString());
                Toast.makeText(BookingSummary.this, "Volley Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        // Add the request to the request queue
        requestQueue.add(getRequest);

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
    public String generateInvoice(String customerName, String mobileNo, String invoiceNo, String bookingDate, String paymentMode, String transactionRefNo, String bookedVehicle, String pickDropTime, String vehicleRentalCharge, String helmetFee, String security, String subtotal, String totalPaid) {

        return "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "  <head>\n" + "    <meta charset=\"UTF-8\" />\n" + "    <title>Bill Generation</title>\n" + "    <style>\n" + "      /* Updated CSS with blue and white theme */\n" + "      body {\n" + "        font-family: Arial, sans-serif;\n" + "        margin: 20px;\n" + "        background-color: #fff; /* Updated background color */\n" + "        color: #333;\n" + "      }\n" + "      .container {\n" + "        width: 80%;\n" + "        margin: 0 auto;\n" + "        background-color: #fff;\n" + "        padding: 20px;\n" + "        border-radius: 8px;\n" + "        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + "      }\n" + "      .header {\n" + "        text-align: center;\n" + "        margin-bottom: 20px;\n" + "      }\n" + "      .logo {\n" + "        max-width: 150px;\n" + "      }\n" + "      table {\n" + "        width: 100%;\n" + "        border-collapse: collapse;\n" + "        margin-bottom: 20px;\n" + "      }\n" + "      th,\n" + "      td {\n" + "        border: 1px solid #cddbf7;\n" + "        padding: 8px;\n" + "        text-align: left;\n" + "      }\n" + "      th {\n" + "        background-color: #cddbf7;\n" + "      }\n" + "      .total {\n" + "        text-align: right;\n" + "        background-color: #cddbf7;\n" + "      }\n" + "      .footer {\n" + "        margin-top: 20px;\n" + "        font-size: 14px;\n" + "        text-align: center;\n" + "        color: #666;\n" + "      }\n" + "      .footer p {\n" + "        margin-bottom: 5px;\n" + "        color: #000000; /* Updated text color to white */\n" + "      }\n" + "      .footer strong {\n" + "        font-weight: bold;\n" + "      }\n" + "    </style>\n" + "  </head>\n" + "  <body>\n" + "    <div class=\"container\">\n" + "      <div class=\"header\">\n" + "        <img\n" + "          class=\"logo\"\n" + "          src=\"https://firebasestorage.googleapis.com/v0/b/bikie-in.appspot.com/o/ic_bikie_rectrangular.jpg?alt=media&token=ac722e1f-fe8f-45fe-948d-8c168c3da94b\"\n" + "          alt=\"Company Logo\"\n" + "        />\n" + "        <h1>Invoice</h1>\n" + "      </div>\n" + "      <table>\n" + "        <tr>\n" + "          <th>Customer Name</th>\n" + "          <td>" + customerName + "</td>\n" + "          <th>Invoice No.</th>\n" + "          <td>" + invoiceNo + "</td>\n" + "        </tr>\n" + "        <tr>\n" + "          <th>Mobile No.</th>\n" + "          <td>" + mobileNo + "</td>\n" + "          <th>Booking Date</th>\n" + "          <td>" + bookingDate + "</td>\n" + "        </tr>\n" + "<tr>\n" + "                                <th>Payment Mode</th>\n" + "                                <td>" + paymentMode + "</td>\n" + "                                <th>Transaction Ref. No.</th>\n" + "                                <td>" + transactionRefNo + "</td>\n" + " </tr>" + "        <tr>\n" + "          <th>Booked Vehicle</th>\n" + "          <td>" + bookedVehicle + "</td>\n" + "          <th>Pick & Drop Time</th>\n" + "          <td>" + pickDropTime + "</td>\n" + "        </tr>\n" + "      </table>\n" + "\n" + "      <table>\n" + "        <tr>\n" + "          <th>Description</th>\n" + "          <th>Price</th>\n" + "        </tr>\n" + "<tr>\n" + "       <td>Vehicle Rental Charge</td>\n" + "       <td>" + vehicleRentalCharge + "</td>\n" + " </tr>\n" + "        <tr>\n" + "          <td>Helmet Charge</td>\n" + "          <td>" + helmetFee + "</td>\n" + "        </tr>\n" + "\n" + "        <tr>\n" + "          <td>Security</td>\n" + "          <td>" + security + "</td>\n" + "        </tr>\n" + "        <tr>\n" + "          <td>Subtotal</td>\n" + "          <td>" + subtotal + "</td>\n" + "        </tr>\n" +

                "\n" + "        <tr class=\"total\">\n" + "          <td><strong>Total Paid</strong></td>\n" + "          <td><strong>" + totalPaid + "</strong></td>\n" + "        </tr>\n" + "      </table>\n" + "    </div>\n" + "    <div class=\"footer\">\n" + "      <p>\n" + "        <em>Instruction:</em> Please reach at the Booked location before 30 mins\n" + "        for better avail of service.\n" + "      </p>\n" + "      <p>\n" + "        <em>Note:</em> Please bring Original Documents for authenticity\n" + "        verification which are opted while booking. For Indian: (Aadhar card\n" + "        along with your Aadhar linked mobile number) & Driving License are\n" + "        mandatory. For non-Indian: Passport/Visa, International driving permit\n" + "        license are mandatory.\n" + "      </p>\n" + "      <p><strong>BIKIEINDIA PRIVATE LIMITED</strong></p>\n" + "      <p>GSTIN : 21GDDPM5414C1Z4</p>\n" + "      <p>Pareswar Sahi,College Road, College Square, Cuttack, Odisha 753003</p>\n" + "      <p>\n" + "        This is an electronically generated invoice and does not require a\n" + "        signature.\n" + "      </p>\n" + "    </div>\n" + "  </body>\n" + "</html>\n";

    }
}