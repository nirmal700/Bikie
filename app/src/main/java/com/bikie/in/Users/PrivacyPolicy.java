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

        String bikiePolicy =
                "\n" +
                "<b>Collection of Personally Identifiable Information</b><br>\n" +
                "&bull; We collect information from you when you place an order or subscribe to our website. When ordering or registering on our site, as appropriate, you may be asked to enter your: name, e-mail address, mailing address, phone number, or credit card information. Our primary goal in doing so is to provide you a safe, efficient, smooth, and customized experience. The information we learn from customers helps us personalize and continually improve your experience of shopping from our web store.<br><br>\n" +
                "\n" +
                "<b>Use of Demographic / Profile Data</b><br>\n" +
                "&bull; We use personal information to provide the services you request. To the extent we use your personal information to market to you, we will provide you the ability to opt-out of such uses. We use your personal information to resolve disputes, troubleshoot problems, help promote a safe service, collect money, measure consumer interest in our products and services, inform you about online and offline offers, products, services, and updates. In our efforts to continually improve our product and service offerings, we collect and analyze demographic and profile data about our user’s activity on our Website.<br><br>\n" +
                "\n" +
                "<b>Cookies</b><br>\n" +
                "&bull; Yes, Cookies are small files that a site or its service provider transfers to your computer’s hard drive through your Web browser (if you allow it) that enables the sites or service providers systems to recognize your browser and capture and remember certain information. We use cookies to help us remember and process the items in your shopping cart. The cookies do not contain any of your personally identifiable information.<br><br>\n" +
                "\n" +
                "<b>Sharing of personal information</b><br>\n" +
                "&bull; Your providing the Information to Bikie and its consequent storage, collection, usage, transfer, access, or processing of the same shall not be in violation of any third-party agreement, laws, charter documents, judgments, orders, and decrees. We may disclose personal information if required to do so by law or in the good faith belief that such disclosure is reasonably necessary to respond to subpoenas, court orders, or other legal processes.<br><br>\n" +
                "\n" +
                "<b>Security Precautions</b><br>\n" +
                "&bull; We strive to ensure the security of your Personal Information and to protect your personal information against unauthorized access or unauthorized alteration, disclosure, or destruction. Whenever you change or access your account information, we offer the use of a secure server.<br><br>\n" +
                "\n" +
                "<b>Choice/Opt-Out</b><br>\n" +
                "&bull; We provide all users with the opportunity to opt-out of receiving non-essential (promotional, marketing-related) communications from us on behalf of our partners, and from us in general, after setting up an account. If we decide to change our privacy policy, we will post those changes on this page.<br><br>\n" +
                "\n" +
                "<b>Permissions</b><br>\n" +
                "&bull; <b>Identity:</b> The identity permission allows the app to access all saved accounts on your device, as well as access and change your personal information stored on the device. By account, we mean everything you will see if you go to Settings > Accounts. There you will probably find a list that will probably contain your Google account, Facebook, WhatsApp, Skype, and many others.<br>\n" +
                "&bull; <b>SMS:</b> Under certain circumstances, we may send you emails and SMS messages when we feel we have an important announcement to share regarding your Service. Please note that your carrier may charge you to receive messages via SMS.<br>\n" +
                "&bull; <b>Photos/Media/Files:</b> Apps usually request Media and Photos permission when it needs External File Storage (either Internal or External) for storing some data, or for some features like sharing images, etc.<br>\n" +
                "&bull; <b>Phones:</b> The phone permission is useful for the app that allows you to place and receive calls within an app.<br>\n" +
                "&bull; <b>Modify phone state</b><br><br>\n" +
                "\n" +
                "<b>Your Approval</b><br>\n" +
                "&bull; By using the Website and/or by providing your information, you consent to the collection and use of the information you disclose on the Website in accordance with this Privacy Policy. If we decide to change our privacy policy, we will post those changes on this page.<br><br>\n" +
                "\n" +
                "<b>Contact Us</b><br>\n" +
                "If there are any questions regarding this privacy policy, you may contact us using the information below.\n";
        privacyPolicy.setText(Html.fromHtml(bikiePolicy, Html.FROM_HTML_MODE_COMPACT));

    }
}