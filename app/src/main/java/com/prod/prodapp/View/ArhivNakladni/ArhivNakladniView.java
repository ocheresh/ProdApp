package com.prod.prodapp.View.ArhivNakladni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.prod.prodapp.Model.DeleteFiles;
import com.prod.prodapp.R;
import com.prod.prodapp.View.DataOfNakladna.DataOfNakladnaView;

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
    public void OnItemClick(final int position, String path) {
        Intent intent = new Intent(ArhivNakladniView.this, DataOfNakladnaView.class);
        intent.putExtra("path_folder", path);
        startActivity(intent);

    }

    @Override
    public void OnItemClickDelete(String path) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Видалення товару");
        alert.setMessage("Видалити дані? ");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("MyError delete", path);
                String temppath = getFilesDir().getAbsolutePath()
                        + "/" + path.replace("+save", "");
                File filezip = new File(temppath + ".zip");
                if (filezip.exists())
                    filezip.delete();
                DeleteFiles deleteFiles = new DeleteFiles(temppath);
                deleteFiles.deleteRecursive(new File(temppath));
                list.clear();
                findPath();
                myAdapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    private String findPath()
    {

        String result = "";
        String path = getFilesDir().getAbsolutePath();
        boolean pl = false;
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("286_2_18_") && !(files[i].getName().contains(".xml"))
                        && !(files[i].getName().contains(".png")) && !(files[i].getName().contains("."))) {
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
                };
            }
        }
        return (result);
    }
}
