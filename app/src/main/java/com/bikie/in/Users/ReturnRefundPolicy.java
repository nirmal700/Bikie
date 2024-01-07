package com.bikie.in.Users;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bikie.in.R;

public class ReturnRefundPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_refund_policy);

        TextView refundPolicy = findViewById(R.id.refundPolicyTextView);

        String bikiePolicy =
                "Effective Date: 15/12/2023<br><br>\n" +
                "\n" +
                "Thank you for choosing Bikie for your transportation needs. We strive to provide you with reliable and comfortable vehicles for your journey. Please review our Return and Refund Policy for vehicle rentals carefully before making a reservation.<br><br>\n" +
                "\n" +
                "<b>1. Reservation Cancellation and Refunds</b><br>\n" +
                "Customers can cancel a reservation without any charge up to 7 days prior to the scheduled pick-up time. Cancellations made within 1.24hrs of the scheduled pick-up may be subject to a cancellation fee. The specific fee will be communicated to the customer at the time of cancellation.<br>\n" +
                "Refunds for cancellations will be processed within 7 working days and credited to the original payment method.<br><br>\n" +
                "\n" +
                "<b>2. Vehicle Return and Inspection</b><br>\n" +
                "Upon returning the rental vehicle, it will be inspected for any damage or excessive wear. Customers are responsible for returning the vehicle in the same condition it was received, excluding normal wear and tear.<br><br>\n" +
                "\n" +
                "<b>3. Refundable Security Deposit</b><br>\n" +
                "A refundable security deposit may be required at the time of vehicle pickup. This deposit is intended to cover any additional charges, such as fuel costs, late fees, or damage to the vehicle. The deposit will be refunded after the vehicle is returned, and deductions, if any, are assessed.<br><br>\n" +
                "\n" +
                "<b>4. Damage or Loss Charges</b><br>\n" +
                "Customers are responsible for any damage to or loss of the rental vehicle during the rental period. The cost of repairs or replacement will be deducted from the security deposit. If the repair costs exceed the deposit, the customer will be billed for the remaining amount.<br><br>\n" +
                "\n" +
                "<b>5. Emergency Roadside Assistance</b><br>\n" +
                "In the event of a breakdown or mechanical issue with the rental vehicle, customers should contact our 24/7 roadside assistance hotline at [emergency contact number]. We will make every effort to provide prompt assistance or arrange for a replacement vehicle.<br><br>\n" +
                "\n" +
                "If you have any questions or concerns regarding our Return and Refund Policy, please contact us using the information below.";

        refundPolicy.setText(Html.fromHtml(bikiePolicy, Html.FROM_HTML_MODE_COMPACT));

    }
}