package com.example.prodapp.View.ImageViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.prodapp.R;
import com.example.prodapp.View.DataOfNakladna.DataOfNakladnaView;

public class FullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Intent i = getIntent();

//        int position = i.getExtras().getInt("id");
//        String main_folder = i.getExtras().getString("main_folder");
//        String photo_folder = i.getExtras().getString("photo_folder");
        ImageAdapter adapter = new ImageAdapter(this, DataOfNakladnaView.correctPathForPhotoFolder, DataOfNakladnaView.correctPathForPhotoPhoto);

        ImageView imageView = findViewById(R.id.fullscreenimage);
        imageView.setImageBitmap(BitmapFactory.decodeFile(adapter.images[ImageActivity.fin_position]));
//        imageView.setImageResource(adapter.images[position]);
    }
}
