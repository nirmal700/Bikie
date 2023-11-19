package com.bikie.in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikie.in.Admin.Edit_Listed_Vehicle;
import com.bikie.in.POJO_Classes.FeaturedBikes;
import com.bikie.in.Users.UserDashboard;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedBikes> featuredBikes;
    private final Context mContext;

    public FeaturedAdapter(Context mContext, ArrayList<FeaturedBikes> featuredBikes) {
        this.mContext = mContext;
        this.featuredBikes = featuredBikes;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_bikes,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        FeaturedBikes featuredBikesPOJO = featuredBikes.get(position);
        Glide.with(mContext).load(featuredBikesPOJO.getmImageURL()).transform(new RoundedCorners(10)).into(holder.image);
        holder.bikeName.setText(featuredBikesPOJO.getmBikeName().toString());
        holder.mDescription.setText(featuredBikesPOJO.getmDescription().toString());


    }

    @Override
    public int getItemCount() {
        return featuredBikes.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView bikeName, mDescription;
        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.featured_image_bike);
            bikeName = itemView.findViewById(R.id.featured_bike_name);
            mDescription = itemView.findViewById(R.id.bikeDescription);
        }
    }
}
