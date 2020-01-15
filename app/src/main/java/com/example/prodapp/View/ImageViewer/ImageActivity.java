package com.example.prodapp.View.ImageViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.prodapp.R;
import com.example.prodapp.View.DataOfNakladna.DataOfNakladnaView;

public class ImageActivity extends AppCompatActivity {

    public static int fin_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        final String main_folder = DataOfNakladnaView.correctPathForPhotoFolder;
        final String photo_folder = DataOfNakladnaView.correctPathForPhotoPhoto;


//        main_folder = getIntent().getExtras().getString("pathfolderall");
//        photo_folder = getIntent().getExtras().getString("pathfolderphoto");

        GridView gridView = findViewById(R.id.gridview_photo);
        gridView.setAdapter(new ImageAdapter(this, main_folder, photo_folder));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FullScreen.class);
//                intent.putExtra("main_folder", main_folder);
//                intent.putExtra("photo_folder", photo_folder);
//                intent.putExtra("id", position);
                fin_position = position;
                startActivity(intent);
            }
        });
    }
}
