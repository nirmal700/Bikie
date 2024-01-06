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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms_and_conditions);
        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);


        String bikieTerms = "<div>\n" +
                "  <div><br /></div>\n" +
                "  <div>\n" +
                "    The terms and conditions of Bikie are the policies that govern the\n" +
                "    relationship between the Company and the User. These terms and conditions\n" +
                "    (as amended from time to time including all Schedules and Annexures hereto)\n" +
                "    shall constitute a legally valid and binding contract under the Applicable\n" +
                "    Laws and the use of any product Services either through Bikie’s Website or\n" +
                "    Mobile Application (hereinafter referred to as App”) by the User shall be\n" +
                "    deemed to be an acceptance of all the terms and conditions set forth herein.\n" +
                "    Users are advised to read these terms and conditions carefully before\n" +
                "    using/registering on the Website/ App or availing Services through the same.\n" +
                "    Bikie reserves the right to alter/modify/change these terms and conditions\n" +
                "    in its sole discretion, from time to time, with or without prior notice to\n" +
                "    the User. Notice of any changes shall be made by Bikie, promptly and\n" +
                "    communicated/brought to the attention of the User in such manner as it deems\n" +
                "    fit.\n" +
                "  </div>\n" +
                "  <div><b>1. Registration Process</b></div>\n" +
                "  <div>\n" +
                "    Every User will have to complete registration formalities as a User either\n" +
                "    through the Website/ App of Bikie. The user needs to fulfill the necessary\n" +
                "    documentation for satisfying the eligibility criteria required to become a\n" +
                "    User. Acceptance of the registration is subject to approval by Bikie at its\n" +
                "    sole discretion.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    Bikie reserves the right to examine and verify the authenticity of the\n" +
                "    documentation provided by the Users.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    The Users acknowledge that the registered mobile number provided during the\n" +
                "    registration shall be used for making requests for services and receiving\n" +
                "    all communications from the Company.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    The User will be responsible for any action taken in pursuance of the\n" +
                "    instruction received from/sent by User mobile shall be of User and deemed to\n" +
                "    have emanated from /confirmed by the User.\n" +
                "  </div>\n" +
                "  <div><b>2. Documents</b></div>\n" +
                "  <div><b>For Domestic Users</b></div>\n" +
                "  <div>\n" +
                "    The following documents will be needed to subscribe to a vehicle and the\n" +
                "    same needs to be uploaded and verified on our Website/App:\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    • Driving License: Original Indian Driving license is required and a learner\n" +
                "    license is not applicable.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    • Aadhar Card/Passport: Original Aadhaar is required, and if your mobile\n" +
                "    number updated in Aadhaar is not the same as your registered mobile number,\n" +
                "    Bikie reserves the right to request for additional documents. Passport is\n" +
                "    also considered as a valid ID and address proof.\n" +
                "  </div>\n" +
                "  <div><b>For International Users</b></div>\n" +
                "  <div>\n" +
                "    The following documents will be needed to subscribe to a vehicle and same\n" +
                "    needs to be uploaded and verified on our Website/App:\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    • Driving License: A valid driving license from their home country along\n" +
                "    with an international driving permit is necessary.\n" +
                "  </div>\n" +
                "  <div><br /></div>\n" +
                "  <div><b>• Valid visa and Passport.</b></div>\n" +
                "  <div><b>Additionally,</b></div>\n" +
                "  <div>\n" +
                "    The user should carry their original documents at the time of the pickup\n" +
                "    which were uploaded on the website/App for verification.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    If a user is unable to verify their identity then Bikie reserve the right to\n" +
                "    deny the service and cancel the booking with the 37.5% cancellation charges\n" +
                "    as per the policy.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    <b>*Note:- Learner’s license is not applicable for renting a vehicle with us.</b>\n" +
                "  </div>\n" +
                "  <div><b>3. Payment process</b></div>\n" +
                "  <div>\n" +
                "    The booking amount has to be paid online while making the booking on our\n" +
                "    website or App. The user has to pay the prescribed amount to avail Bikie’s\n" +
                "    service and the amount can be changed as per Bikie’s discretion. In case of\n" +
                "    any extension required, the user will have to pay the additional amount\n" +
                "    through the mobile and web application subject to availability.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    You can use any of the options below for the payment not limited to the\n" +
                "    below mentioned :\n" +
                "  </div>\n" +
                "  <ul>\n" +
                "    <li><b>NetBanking</b></li>\n" +
                "    <li><b>UPI</b></li>\n" +
                "    <li><b>PAYTM</b></li>\n" +
                "    <li><b>Debit Card</b></li>\n" +
                "    <li><b>Credit Card.</b></li>\n" +
                "  </ul>\n" +
                "  <div>\n" +
                "    The Payment Gateway and other transactional charges will be governed by the\n" +
                "    Government and the rules defined by the competent authorities.\n" +
                "  </div>\n" +
                "  <div>\n" +
                "    <b>Vouchers - Any voucher issued by the company will be exclusive of taxes\n" +
                "    however the tax amount will be adjusted in the next booking automatically.</b>\n" +
                "  </div>\n" +
                "  <div><b>4. Delay in Drop Offs</b></div>\n" +
                "  <div>\n" +
                "    A fee of Rs. 100 along with 3X of the per hour rental charges will be\n" +
                "    charged for vehicles being returned after the grace period of 30 mins.\n" +
                "  </div>\n" +
                "  <div><b>5. Use of Vehicle</b></div>\n" +
                "  <ul>\n" +
                "    <li>\n" +
                "      The bookings have to be made by the User for their own usage and not for or\n" +
                "      on behalf of any other user. The User will be solely responsible for the\n" +
                "      usage of the vehicle and the User will be at all times responsible for the\n" +
                "      vehicle under their possession during the duration of rental. During the\n" +
                "      rental period, any breach of the terms and conditions will result in action\n" +
                "      against the User for material breach of this agreement. The company does not\n" +
                "      guarantee the availability of any vehicle at any given time however the\n" +
                "      Company promises to look after the best interest of the User. The user has\n" +
                "      to follow the under mentioned terms and conditions :\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      These scooters and motorcycles we rent are large, heavyweight and powerful\n" +
                "      and need to be handled with care. In case the rider/user is a beginner, they\n" +
                "      have to be extra careful in handling the vehicle.\n" +
                "    </li>\n" +
                "    <li><b>User/Rider Age :</b></li>\n" +
                "    <li>Between 18 - 20 :</li>\n" +
                "    <li>\n" +
                "      Will be allowed to ride any of our vehicles below 200cc, except the Yamaha\n" +
                "      R15.\n" +
                "    </li>\n" +
                "    <li><b>21 and Above :</b></li>\n" +
                "    <li>Will be allowed to ride any of our vehicles, including superbikes.</li>\n" +
                "    <li>\n" +
                "      The rider must always wear a helmet while riding. Appropriate riding gear,\n" +
                "      i.e... Boots or closed-toe shoes, pants & eyewear is advisable.\n" +
                "    </li>\n" +
                "    <!-- Add the rest of the points in the 'Use of Vehicle' section -->\n" +
                "  </ul>\n" +
                "<div><b>6. Prohibited Uses</b></div>\n" +
                "  <ul>\n" +
                "    <li>The user agrees to use the vehicle for personal use only.</li>\n" +
                "    <li>The user will not carry load or passenger more than the prescribed limit by the authorities.</li>\n" +
                "    <li>Our motorcycles(Vehicles) cannot be used for rallies and rally surcharges or any format of professional or amateur competitions or for performing stunts.</li>\n" +
                "    <li>By any person who is under the influence of (i) alcohol or (ii) any drug or medication under the effects of which the operation of a vehicle is prohibited or not recommended.</li>\n" +
                "    <li>In carrying out any crime or any other illegal activity as deemed illegal by the LAW.</li>\n" +
                "    <li>The user is not allowed to make any modifications, internal or external to the motorcycle. Any additional fittings on the vehicle will lead to prohibition of the user from usage of the motorcycle any further.</li>\n" +
                "    <li>Minors will not be provided with any vehicle from Bikie and the same is expected from the users. The company will not be responsible in case the vehicle is shared with a minor.</li>\n" +
                "    <li>Any sharp objects, weapons or illegal substances are prohibited from being carried on any Bikie vehicle.</li>\n" +
                "  </ul>\n" +
                "  <div><b>7. Delivery process</b></div>\n" +
                "  <ul>\n" +
                "    <li>We will connect with the user to schedule the delivery of the vehicle.</li>\n" +
                "    <li>We request that the customer be present at the agreed date and time to receive the vehicle at the decided location.</li>\n" +
                "    <li>User/Rider should do a test ride of the bike before they accept the vehicle from the Lessor.</li>\n" +
                "    <li>The Delivered vehicles cannot be rejected after handover.</li>\n" +
                "    <li>Lessee can request for exchange of the vehicle if not satisfied with the vehicle condition.</li>\n" +
                "    <li>Though we do quality checks at our end before delivery, the user/rider is expected to see if there are any damages and report the same to the representative of the lessor and photos shall be captured of the same.</li>\n" +
                "  </ul>\n" +
                "  <div><b>8. Right to refuse the service</b></div>\n" +
                "  <ul>\n" +
                "    <li>The company holds the right of refusal to any client not deemed fit to be served by the company or its authorized staff.</li>\n" +
                "    <li>Without prejudice to its rights, claims and contentions under the law and equity, Bikie reserves the right to terminate/suspend the Users’ registration and/or withdraw Services rendered to the User and/or levy a penalty, in the event Users indulge in using/operating the Vehicles in a manner prohibited by Bikie.</li>\n" +
                "  </ul>"+
                "<div><b>9. Damage policy</b></div>\n" +
                "  <ul>\n" +
                "    <li>The Lessee agrees to pay for any damage to, loss of, or any theft (disappearance) of parts of the vehicle, regardless of cause or fault. Items damaged beyond repair will be paid for at their Market Price.</li>\n" +
                "    <li>The representative shall check the bike and its parts to ascertain any damage. Damage shall be defined as follows:</li>\n" +
                "    <ul>\n" +
                "      <li>Any damage which existed prior to the handover of the vehicle and was agreed between the lessee and lessor will not be chargeable to the Lessee.</li>\n" +
                "      <li>Tears in the seat cover will result in a charge towards a replacement of the seat cover. Opening up a stitched joint will not be chargeable.</li>\n" +
                "      <li>Any variation showing damages, if ascertained as not caused by normal wear and tear, would be charged and would have to be borne by the Lessee.</li>\n" +
                "    </ul>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>10. Fuel Policy</b></div>\n" +
                "  <ul>\n" +
                "    <li>Fuel is not included in the rental amount, and sufficient fuel is provided to reach the nearest fuel station.</li>\n" +
                "    <li>No refund will be provided if extra fuel is leftover at the time of returning the vehicle.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>11. Process (communication)</b></div>\n" +
                "  <ul>\n" +
                "    <li>Bikie may send booking confirmation, itinerary information, cancellation, payment confirmation, refund status, schedule change, or any other relevant information via SMS, voice call, or email.</li>\n" +
                "    <li>Communications via SMS and/or voice call by Bikie are transactional and not considered as unsolicited commercial communication as per the guidelines of Telecom Regulation Authority of India (TRAI).</li>\n" +
                "    <li>The User agrees to indemnify Bikie against losses and damages incurred due to any action taken by TRAI, Access Providers, or any other authority based on erroneous complaints made by the User.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>12. Maintenance</b></div>\n" +
                "  <ul>\n" +
                "    <li>The user is liable for checking engine oils during a trip and maintaining the vehicle while on the road.</li>\n" +
                "    <li>Any mechanical failures due to negligence in normal maintenance by the customer may result in holding the customer responsible.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>13. Cancellation policy</b></div>\n" +
                "  <ul>\n" +
                "    <li><b>Short term bookings:</b></li>\n" +
                "    <ul>\n" +
                "      <li>No show - 100% deduction, and only the security deposit will be refunded.</li>\n" +
                "      <li>Between 0 - 6 hrs of the pick-up time - 75% rental charges will be withheld, and the security deposit will be refunded.</li>\n" +
                "      <li>Between 6-24 hrs of the pick-up time: 50% rental charges will be withheld.</li>\n" +
                "      <li>Between 24-72 hrs of the pick-up time: 25% rental charges will be withheld.</li>\n" +
                "      <li>72 hrs or more prior to the pick-up time: 10% of rental charges will be withheld.</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <li><b>Cancellation due to document issue:</b></li>\n" +
                "    <ul>\n" +
                "      <li>37.5% of the rental amount will be deducted if:</li>\n" +
                "      <ul>\n" +
                "        <li>Customer is not willing to submit the document as per the terms and conditions.</li>\n" +
                "        <li>Customer is not able to verify their identity.</li>\n" +
                "        <li>Customer documents are not valid.</li>\n" +
                "      </ul>\n" +
                "    </ul>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>14. Extra charges</b></div>\n" +
                "  <ul>\n" +
                "    <li><b>Helmet:</b> If a user chooses an extra helmet on the spot, it will be billed additionally on the spot.</li>\n" +
                "    <li><b>Excess Km:</b> The excess km charges will be calculated at the time of drop-off, and the user will be required to pay the same at the location.</li>\n" +
                "    <li><b>Damages:</b> Damages will be inspected at the drop-off time, and the charges will be collected on the spot as per the damage terms and conditions.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>15. Refunds</b></div>\n" +
                "  <ul>\n" +
                "    <li><b>Security Deposit:</b> Refundable security deposit for certain vehicles. The refund usually takes 3-7 working days to reflect in the source account from the date of invoice after the trip has ended.</li>\n" +
                "    <li><b>Other refunds:</b> Any refund processed by the company usually takes 3-7 working days. Note: No refunds will be issued in cases of early drop-offs.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>16. Over-speeding</b></div>\n" +
                "  <ul>\n" +
                "    <li>Bikie vehicles must be ridden within permissible limits. Speed limits are communicated during booking and in confirmation emails. Users exceeding speed limits may be penalized up to Rs. 500 for every third instance of rash driving.</li>\n" +
                "    <li>All Bikie vehicles have GPS tracking devices, and users are advised to follow speed guidelines.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>17. Exceeding Km limit</b></div>\n" +
                "  <ul>\n" +
                "    <li>Extra charges apply if the Km limit is exceeded by the user. The final amount will be conveyed at the time of returning the vehicle by our fleet executive.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>18. Helmet</b></div>\n" +
                "  <ul>\n" +
                "    <li>One complementary helmet is provided without any charge.</li>\n" +
                "    <li>Extra helmets can be added while making short-term bookings. Customers can rent only 2 helmets per booking. An additional helmet will be charged as per the company's rates. Damaged or lost helmets will incur a charge of Rs. 900.</li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <div><b>19. Use of Information</b></div>\n" +
                "  <ul>\n" +
                "    <li>The user's first name and booking details may be used for showing booking pop-ups to other users on the homepage. No other personally identifiable information will be shown.</li>\n" +
                "    <li>Refer to the Privacy Policy for more details regarding the use of information.</li>\n" +
                "  </ul>\n" +
                "</div>\n";

        termsAndConditionsTextView.setText(Html.fromHtml(bikieTerms, Html.FROM_HTML_MODE_COMPACT));
    }
}