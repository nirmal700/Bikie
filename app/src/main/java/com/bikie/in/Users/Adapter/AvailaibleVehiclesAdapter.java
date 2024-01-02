package com.bikie.in.Users.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bikie.in.Admin.Adapter.ListedVehiclesAdapter;
import com.bikie.in.POJO_Classes.NewVehicle;
import com.bikie.in.R;
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

import java.util.List;
import java.util.Locale;

public class AvailaibleVehiclesAdapter extends RecyclerView.Adapter<AvailaibleVehiclesAdapter.ImageViewHolder> {
    private final Context mContext;
    private final List<NewVehicle> availaiblevehicleDataList;
    private OnItemClickListener mListener;
    Timestamp mrequestedpickupDateTimeStamp, mrequesteddropoffDateTimeStamp;

    public AvailaibleVehiclesAdapter(Context context, List<NewVehicle> vehicleData, Timestamp requestedpickupDateTimeStamp,Timestamp requesteddropoffDateTimeStamp) {

        mContext = context;
        availaiblevehicleDataList = vehicleData;
        mrequestedpickupDateTimeStamp = requestedpickupDateTimeStamp;
        mrequesteddropoffDateTimeStamp = requesteddropoffDateTimeStamp;
        Log.e("TAG", "AvailaibleVehiclesAdapter: "+ availaiblevehicleDataList.get(0).getmVehicleRent24Hr());
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_availaible_vehicles, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        NewVehicle currentData = availaiblevehicleDataList.get(position);
        holder.tv_name.setText(currentData.getmVehicleName());
        holder.tv_location.setText(currentData.getmVehicleLocation());
        double calculatedPrice = calculatePrice(availaiblevehicleDataList.get(position), mrequestedpickupDateTimeStamp, mrequesteddropoffDateTimeStamp);
        String formattedPrice = String.format(Locale.US, "₹ %.2f", calculatedPrice);
        holder.tv_price.setText(formattedPrice);


        Glide.with(mContext)
                .load(currentData.getmVehicleImages().get(0))
                .transform(new RoundedCorners(10))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.animationView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.animationView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return availaiblevehicleDataList.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public LottieAnimationView animationView;
        public TextView tv_location, tv_name,tv_price;

        MaterialCardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bike_image);
            tv_name = itemView.findViewById(R.id.bike_name);
            tv_location = itemView.findViewById(R.id.bike_location);
            animationView = itemView.findViewById(R.id.lottieAnimationView);
            cardView = itemView.findViewById(R.id.mCardViewListed);
            tv_price = itemView.findViewById(R.id.bike_price_bold);
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
    private double calculatePrice(NewVehicle vehicle, Timestamp pickupTime, Timestamp dropoffTime) {
        // Calculate the duration in hours
        long durationInMillis = dropoffTime.getSeconds() - pickupTime.getSeconds();
        double durationInHours = durationInMillis / 3600.0;

        // Determine the appropriate pricing tier based on the duration
        double price;
        if (durationInHours <= 1) {
            price = vehicle.getmVehicleRent1Hr();
        } else if (durationInHours <= 3) {
            price = (double) vehicle.getmVehicleRent3Hr() /3.00;
        } else if (durationInHours <= 6) {
            price = (double) vehicle.getmVehicleRent6Hr() /6.00;
        } else if (durationInHours <= 12) {
            price = (double) vehicle.getmVehicleRent12Hr() /12.00;
        } else {
            price = (double) vehicle.getmVehicleRent24Hr() /24.00;
        }

        // Calculate the total price

        double calculatedPrice = price * durationInHours;

        // Log the relevant fields and the calculated price
        System.out.println("Duration in hours: " + durationInHours);
        System.out.println("Hourly Rate for 1 hour: " + vehicle.getmVehicleRent1Hr());
        System.out.println("Hourly Rate for 3 hours: " + vehicle.getmVehicleRent3Hr());
        System.out.println("Hourly Rate for 6 hours: " + vehicle.getmVehicleRent6Hr());
        System.out.println("Hourly Rate for 12 hours: " + vehicle.getmVehicleRent12Hr());
        System.out.println("Hourly Rate for 24 hours: " + vehicle.getmVehicleRent24Hr());
        System.out.println("Calculated Price: " + calculatedPrice);

        return calculatedPrice;
    }


}
