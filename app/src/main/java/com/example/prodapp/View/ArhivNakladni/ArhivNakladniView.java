package com.example.prodapp.View.ArhivNakladni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prodapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArhivNakladniView extends AppCompatActivity implements AdapterArhivNakladna.OnItemListener {

    RecyclerView recyclerView;
    AdapterArhivNakladna myAdapter;

    List<String> list = new ArrayList<>();

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 10;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arhiv_nakladni_view);

        verifyStoragePermissions(this);

        recyclerView = findViewById(R.id.recyclearchiv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Toast.makeText(this, findPath(), Toast.LENGTH_SHORT).show();
        findPath();

        myAdapter = new AdapterArhivNakladna(ArhivNakladniView.this, list, this);
        recyclerView.setAdapter(myAdapter);



    }


    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    PERMISSION_REQUEST_STORAGE
            );
        }
    }

    @Override
    public void OnItemClick(final int position) {

    }

    private String findPath()
    {

 //       /data/data/com.example.prodapp/files
        String result = "";
        String path = getFilesDir().getAbsolutePath();
        boolean pl = false;
//        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("286_2_18_") && !(files[i].getName().contains(".xml"))
                        && !(files[i].getName().contains(".png"))) {
                File directory_temp = new File(files[i].getAbsolutePath());
                File[] files_temp = directory_temp.listFiles();
                for (int t = 0; t < files_temp.length; t++) {
                    if (files_temp[t].getName().equals("readme")) {
                        list.add(files[i].getName() + "+save");
                        pl = true;
                    }
                }
                if (pl == false)
                    list.add(files[i].getName());
                pl = false;
//                result += files[i].getName() + "\n";
                }
//                result += files[i].getName() + "\n";
            }
        }
        return (result);
    }
}
