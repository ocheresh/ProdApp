package com.example.prodapp.View.ImageViewer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.prodapp.R;
import com.example.prodapp.View.DataOfNakladna.DataOfNakladnaView;

import java.io.File;
import java.util.Iterator;

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

        final ImageAdapter imgAdap = new ImageAdapter(this, main_folder, photo_folder);

        GridView gridView = findViewById(R.id.gridview_photo);
        gridView.setAdapter(imgAdap);

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

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ImageActivity.this);
                alert.setTitle("Видалення фотографії");
                alert.setMessage("Видалити фотографію? ");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        File file = new File(getFilesDir().getAbsolutePath() + "/"
//                                + DataOfNakladnaView.correctPathForPhotoFolder + "/" + DataOfNakladnaView.correctPathForPhotoPhoto
//                        + "/" + imgAdap.images[position]);
                        File file = new File(imgAdap.images[position]);
                        if (file.exists()) {
                            boolean del = file.delete();
//                            Toast.makeText(ImageActivity.this, String.valueOf(del), Toast.LENGTH_SHORT).show();
                            if (del) {
                                for (int i = 0; i < DataOfNakladnaView.list_uri_img.size(); i++) {
//                                    Toast.makeText(ImageActivity.this, imgAdap.images[position], Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ImageActivity.this, (getFilesDir().getPath().replace("/files", "") + DataOfNakladnaView.list_uri_img.get(i).getPath()), Toast.LENGTH_SHORT).show();
                                    if ((imgAdap.images[position]).equalsIgnoreCase((getFilesDir().getPath().replace("/files", "") + DataOfNakladnaView.list_uri_img.get(i).getPath()))) {
                                        DataOfNakladnaView.list_uri_img.remove(i);
//                                        Toast.makeText(ImageActivity.this, imgAdap.images[position], Toast.LENGTH_SHORT).show();
                                        i = DataOfNakladnaView.list_uri_img.size();
                                    }
                                }
                                finish();
                                startActivity(getIntent());
                            }
                        }
                        else
                            Toast.makeText(ImageActivity.this, file.getPath(), Toast.LENGTH_LONG).show();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                return true;
            }
        });
    }
}
