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

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        TextView privacyPolicy = findViewById(R.id.privacyPolicyTextView);
        String bikiePolicy = getString(R.string.bikie_privacy_policy);
        privacyPolicy.setText(Html.fromHtml(bikiePolicy, Html.FROM_HTML_MODE_COMPACT));
    }
}