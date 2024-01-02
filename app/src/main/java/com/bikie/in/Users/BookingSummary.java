package com.bikie.in.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bikie.in.R;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import kotlin.text.Charsets;

public class BookingSummary extends AppCompatActivity {

    Button mProceedToPayment;
    String apiEndPoint = "/pg/v1/pay";
    String merchantID = "PGTESTPAYUAT";
    String merchantTransactionID = "MT7850554277434568134288104";
    private static int B2B_PG_REQUEST_CODE = 777;
    String base64String;
    String checksum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        PhonePe.init(BookingSummary.this, PhonePeEnvironment.UAT, "MERCHANTUAT", "");


        mProceedToPayment = findViewById(R.id.btn_proceedToPayment);


        Map<String, Object> merchantRequestMap = new HashMap<>();
        merchantRequestMap.put("merchantId", "PGTESTPAYUAT");
        merchantRequestMap.put("merchantTransactionId", "MT7850590068188104");
        merchantRequestMap.put("merchantUserId", "MUID676776123");
        merchantRequestMap.put("amount", 10000);
        merchantRequestMap.put("callbackUrl", "https://webhook.site/3891c569-8b9c-45db-9946-62adc780307c");
        merchantRequestMap.put("mobileNumber", "9999999999");

        // Create the nested paymentInstrument map
        Map<String, Object> paymentInstrumentMap = new HashMap<>();
        paymentInstrumentMap.put("type", "PAY_PAGE");

        merchantRequestMap.put("paymentInstrument", paymentInstrumentMap);

        // Convert the HashMap to a JSON object
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(merchantRequestMap).getAsJsonObject();

        // Print the JSON object
        System.out.println("JSON Object: " + jsonObject);

        // Convert the JSON object to a string
        String jsonString = jsonObject.toString();

        // Convert the JSON string to bytes using UTF-16
        base64String = Base64.getEncoder().encodeToString(jsonString.getBytes(Charsets.UTF_8));

        // Print the resulting Base64 string
        System.out.println("BASE64 String: " + base64String);

        try {
             checksum = convertToSHA256(base64String + apiEndPoint + "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399") + "###1" ;
            System.out.println("SHA-256 Hash: " + checksum);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(BookingSummary.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }


        mProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog dialog = new Dialog(BookingSummary.this);
//
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.terms_and_conditions);
//
//                dialog.show();
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
                B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                        .setData(base64String)
                        .setChecksum(checksum)
                        .setUrl(apiEndPoint)
                        .build();
                Log.e("B2G", "Request: "+b2BPGRequest );
                Log.e("B2G", "CheckSum: "+checksum );
                Log.e("B2G", "ApiEndPoint: "+apiEndPoint );
                Log.e("B2G", "BASE64: "+base64String );

                try {
                    startActivityForResult(PhonePe.getImplicitIntent(BookingSummary.this, b2BPGRequest, ""),B2B_PG_REQUEST_CODE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

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

    private void CheckPaymentStatus() {
        String mStatuschecksum = convertToSHA256("/pg/v1/status/"+merchantID+"/"+merchantTransactionID+"099eb0cd-02cf-4e2a-8aca-3e6c6aff0399")+"###1";
        Log.e("B2G", "CheckPaymentStatus: "+mStatuschecksum );
        Log.e("B2G", "CheckPaymentString: "+"/pg/v1/status/"+merchantID+"/"+merchantTransactionID+"099eb0cd-02cf-4e2a-8aca-3e6c6aff0399" );
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
}