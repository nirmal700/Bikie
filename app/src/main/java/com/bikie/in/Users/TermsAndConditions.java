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

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);


        String bikieTerms = getString(R.string.terms_conditons);

        termsAndConditionsTextView.setText(Html.fromHtml(bikieTerms, Html.FROM_HTML_MODE_COMPACT));
    }
}