package com.bikie.in;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bikie.in.POJO_Classes.FeaturedBikes;
import com.bikie.in.POJO_Classes.FeaturedTestimonials;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.TestimonialViewHolder> {

    ArrayList<FeaturedTestimonials> featuredTestimonials;
    private final Context mContext;

    public TestimonialAdapter( ArrayList<FeaturedTestimonials> featuredTestimonials, Context mContext) {
        this.featuredTestimonials = featuredTestimonials;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TestimonialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_testimonials_card,parent,false);
       TestimonialViewHolder testimonialViewHolder = new TestimonialViewHolder(view);
        return testimonialViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonialViewHolder holder, int position) {
        FeaturedTestimonials testimonials = featuredTestimonials.get(position);
        Glide.with(mContext).load(testimonials.getmTestimonialImageURL()).transform(new RoundedCorners(10)).into(holder.image);
        holder.title.setText(testimonials.getmUserName().toString());
        holder.description.setText(testimonials.getmReview().toString());
    }

    @Override
    public int getItemCount() {
        return featuredTestimonials.size();
    }

    public static class TestimonialViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,description;
        public TestimonialViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image_testimonial);
            title = itemView.findViewById(R.id.featured_user_name);
            description = itemView.findViewById(R.id.reviewDescription);
        }
    }
}
