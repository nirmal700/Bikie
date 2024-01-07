package com.bikie.in.Users.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bikie.in.Admin.Adapter.ListedVehiclesAdapter;
import com.bikie.in.POJO_Classes.BookingConfirmation;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
import com.bikie.in.Users.BookingSummary;
import com.bikie.in.Users.VehicleInformationToBook;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ImageViewHolder> {
    private final Context mContext;
    private final List<BookingConfirmation> bookingConfirmationList;
    private OnItemClickListener mListener;

    public BookingHistoryAdapter(Context context, List<BookingConfirmation> bookingConfirmations) {

        mContext = context;
        bookingConfirmationList = bookingConfirmations;
        Log.e("Adapter", "BookingHistoryAdapter: "+bookingConfirmations );
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_booking_history, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        BookingConfirmation currentData = bookingConfirmationList.get(position);
        holder.mVehicleName.setText(currentData.getmVehicleName());
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);
        // Format for date and time
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        try {
            Date pickupDateTime = dateTimeFormat.parse(convertTimestampToString(currentData.getmPickupDate()));
            assert pickupDateTime != null;
            holder.mPickupDate.setText(displayDateFormat.format(pickupDateTime));
            holder.mPickupTime.setText(displayTimeFormat.format(pickupDateTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            Date dropoffDateTime = dateTimeFormat.parse(convertTimestampToString(currentData.getmDropOffDate()));
            assert dropoffDateTime != null;
            holder.mDropOffDate.setText(displayDateFormat.format(dropoffDateTime));
            holder.mDropOffTime.setText(displayTimeFormat.format(dropoffDateTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Glide.with(mContext).load(currentData.getmVehicleImage())
//                .placeholder(R.drawable.sand_clock)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return bookingConfirmationList.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView mPickupDate,mPickupTime,mDropOffDate,mDropOffTime, mVehicleName,mBookingStatus;
        public Button mViewDetails,mNavigate;

        MaterialCardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bike_image);
            mPickupDate = itemView.findViewById(R.id.mPickupDate);
            mPickupTime = itemView.findViewById(R.id.mPickupTime);
            mDropOffDate = itemView.findViewById(R.id.mDropoffDate);
            mDropOffTime = itemView.findViewById(R.id.mDropoffTime);
            mVehicleName = itemView.findViewById(R.id.bike_name);
//            mBookingStatus = itemView.findViewById(R.id.bike_price_bold);
            mViewDetails = itemView.findViewById(R.id.mViewDetails);
            mNavigate = itemView.findViewById(R.id.mNavigate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    // Add a method to calculate the price based on start and end times

    public static String convertTimestampToString(com.google.firebase.Timestamp timestamp) {
        // Assuming timestamp is of type java.sql.Timestamp
        Date date = timestamp.toDate();

        // Choose the desired date and time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:mm a", Locale.US);

        // Convert the Date object to a formatted string
        return dateFormat.format(date);
    }


}
