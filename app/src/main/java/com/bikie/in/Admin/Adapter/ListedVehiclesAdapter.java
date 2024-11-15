package com.bikie.in.Admin.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ListedVehiclesAdapter extends RecyclerView.Adapter<ListedVehiclesAdapter.ImageViewHolder> {
    private final Context mContext;
    private final List<NewVehicle> ListedvehicleDataList;
    private OnItemClickListener mListener;

    public ListedVehiclesAdapter(Context context, List<NewVehicle> vehicleData) {

        mContext = context;
        ListedvehicleDataList = vehicleData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_listed_vehicles, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        NewVehicle currentData = ListedvehicleDataList.get(position);
        holder.tv_name.setText(currentData.getmVehicleName());
        holder.tv_location.setText(currentData.getmVehicleLocation());


        holder.mAvailaible.setOnCheckedChangeListener(null);
        holder.mAvailaible.setChecked(currentData.getmIsAvailable());

        String transitionName = "sharedCard" + position;
        holder.cardView.setTransitionName(transitionName);

        holder.mAvailaible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("Vehicles").document(currentData.getmVehicleId());
                docRef.update("mIsAvailable", isChecked) // Set to true or false as needed
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(mContext, "Successfully changed the state!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(mContext, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
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
        return ListedvehicleDataList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public LottieAnimationView animationView;
        public TextView tv_location, tv_name;
        SwitchMaterial mAvailaible;
        MaterialCardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.Vehicle_image);
            tv_name = itemView.findViewById(R.id.Vehicle_name);
            tv_location = itemView.findViewById(R.id.Vehicle_location);
            animationView = itemView.findViewById(R.id.lottieAnimationView);
            mAvailaible = itemView.findViewById(R.id.switchAvailaible);
            cardView = itemView.findViewById(R.id.mCardViewListed);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position, cardView.getTransitionName());
                }
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, String transitionName);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}

