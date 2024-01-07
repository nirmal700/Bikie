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

        String bikiePolicy = getString(R.string.return_refund_policy_string);
        refundPolicy.setText(Html.fromHtml(bikiePolicy, Html.FROM_HTML_MODE_COMPACT));

    }
}