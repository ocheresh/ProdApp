package com.example.prodapp.View.ChoiseMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.example.prodapp.Model.QrCodeParser.QrCodeParser;
import com.example.prodapp.Presenter.ChoiseMenu.ChoiseMenuPresenter;
import com.example.prodapp.Presenter.ChoiseMenu.IChoiseMenuPresenter;
import com.example.prodapp.Presenter.DataOfNakladna.DataOfNakladnaPresenter;
import com.example.prodapp.R;
import com.example.prodapp.View.ArhivNakladni.ArhivNakladniView;
import com.example.prodapp.View.ChooseProduct.ChooseProductView;
import com.example.prodapp.View.DataOfNakladna.DataOfNakladnaView;
import com.example.prodapp.View.InfoOfNakladna.InfoOfNakladnaProdView;
import com.example.prodapp.View.Settings.SettingView;

public class ChoiseMenuView extends AppCompatActivity implements IChoiseMenuView {

    Button butCreatePrihod;
    Button butArhiv;
//    Button butScanPrihod;
    static public QrCodeParser qrCodeParser = null;
    IChoiseMenuPresenter iChoiseMenuPresenter;
//    String parser = "286_2_18_178_2/29.10.2019/ВійськСервіс/145/15.11.2019/GAZ/AV2585KL/Шумахер Антон Борисович/1014/20/1018/20/1023/20/1028/20/1030/20/1033/20/1034/20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_menu);

        iChoiseMenuPresenter = new ChoiseMenuPresenter(this);

        butArhiv = findViewById(R.id.choiseFile);
        butArhiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ChoiseMenuView.this, ArhivNakladniView.class);
                    startActivity(intent);

                } catch (Exception e) {
                }
            }
        });

        butCreatePrihod = findViewById(R.id.createPrihod);
        butCreatePrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iChoiseMenuPresenter.onCreateNakladna();
            }
        });

//        butScanPrihod = findViewById(R.id.scanPrihod);
//        butScanPrihod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
//
//                    startActivityForResult(intent, 0);
//
//                } catch (Exception e) {
//
//                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android&hl=uk");
//                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
//                    startActivity(marketIntent);
//
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
//                Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
                qrCodeParser = new QrCodeParser(contents);
                Intent intent = new Intent(ChoiseMenuView.this, InfoOfNakladnaProdView.class);
                startActivity(intent);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_settings:
                iChoiseMenuPresenter.onSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void pressCreateNaladna() {
        Intent intent = new Intent(ChoiseMenuView.this, InfoOfNakladnaProdView.class);
//        Intent intent = new Intent(ChoiseMenuView.this, QrCodeReader.class);
        startActivity(intent);
    }

    @Override
    public void pressSettings() {
        Intent intent = new Intent(ChoiseMenuView.this, SettingView.class);
        startActivity(intent);
    }
}
