package com.urban.urbanreport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class Fimage_popup extends AppCompatActivity {

    private ImageButton img_back;
    private PhotoView img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fimage_popup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img_back = (ImageButton) findViewById(R.id.img_back);
        img_view = (PhotoView) findViewById(R.id.img_view);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().hasExtra("url")) {
            String urlgambar = getIntent().getExtras().getString("url");
            Glide.with(Fimage_popup.this).load(urlgambar)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(img_view);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}