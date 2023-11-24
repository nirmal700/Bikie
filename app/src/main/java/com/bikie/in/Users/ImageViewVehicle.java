package com.bikie.in.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bikie.in.R;
import com.bumptech.glide.Glide;

public class ImageViewVehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_vehicle);

        ImageView imageView = findViewById(R.id.imageView_Vehicle);

        Glide.with(ImageViewVehicle.this).load(getIntent().getStringExtra("image")).into(imageView);
    }
}