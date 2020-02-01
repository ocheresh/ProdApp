package com.example.prodapp.View.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prodapp.Model.Employe.Employe;
import com.example.prodapp.Presenter.Settings.ISettingsPresenter;
import com.example.prodapp.Presenter.Settings.SettingsPresenter;
import com.example.prodapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class SettingView extends AppCompatActivity implements ISettingView{

    ISettingsPresenter iSettingsPresenter;

    TextView textViewName;
    TextView textViewSurname;
    EditText editTextNumber;
    EditText editTextRank;
    EditText editTextUnit;
    EditText textViewSendEmail;
    TextView textnumbertel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_view);

        verifyStoragePermission();

        textViewName = findViewById(R.id.textViewSetName2);
        textViewSurname = findViewById(R.id.textViewSetSurname2);
        editTextNumber = findViewById(R.id.textViewSetNumber2);
        editTextRank = findViewById(R.id.textViewSetRank2);
        editTextUnit = findViewById(R.id.textViewSetMilitaryUnit2);
        textViewSendEmail = findViewById(R.id.textViewSetEmailToSend2);
        textnumbertel = findViewById(R.id.telephoneUser);

//        textnumbertel.setText(readTelNumber());

        iSettingsPresenter = new SettingsPresenter(this);
        iSettingsPresenter.onReadInfo();
        iSettingsPresenter.onSetInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.confirm, menu);
        return true;
    }

    private void verifyStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                        Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE};
                requestPermissions(permission, 1000);
            }
        }
    }

    private String readTelNumber()
    {
        Log.i("MyError", "start");
        try {
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            Log.i("MyError", "start2");
            Log.i("MyError", tMgr.getLine1Number());
            return (tMgr.getLine1Number());
        }
        catch (SecurityException e){
            Log.i("MyError", e.getMessage());
        }

        return ("sdasdasda");
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.app_bar_confirm:
//                iSettingsPresenter.onSaveInfo();
//                Toast.makeText(this, "Дані збережені", Toast.LENGTH_SHORT).show();
//                this.onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void save_settings(View v)
    {
        iSettingsPresenter.onSaveInfo();
        Toast.makeText(this, "Дані збережені", Toast.LENGTH_SHORT).show();
        this.onBackPressed();
    }

    @Override
    public void pressRead(Employe employe) {
        XmlPullParserFactory parserFactory;
        try {

            FileInputStream is= getApplicationContext().openFileInput("data_user.xml");
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,null);

            processParsing_userData(parser, employe);

            is.close();
        }
        catch (XmlPullParserException e) {e.getMessage();}
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void pressSetInfo(Employe employe) {
        textnumbertel.setText(readTelNumber());
        textViewName.setText(employe.getFirstName());
        textViewSurname.setText(employe.getLastName());
        editTextNumber.setText(employe.getContactNo());
        editTextRank.setText(employe.getMilitaryRank());
        editTextUnit.setText(employe.getMilitaryUnit());
        textViewSendEmail.setText(employe.getEmailToSend());
    }

    @Override
    public void pressSaveInfo(Employe employe) {
        try {
            FileOutputStream fileos= getApplicationContext().openFileOutput("data_user.xml", Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");
            xmlSerializer.startTag(null, "FirstName");
            xmlSerializer.text(employe.getFirstName());
            xmlSerializer.endTag(null, "FirstName");
            xmlSerializer.startTag(null, "LastName");
            xmlSerializer.text(employe.getLastName());
            xmlSerializer.endTag(null, "LastName");
            xmlSerializer.startTag(null,"ContactNo");
            xmlSerializer.text(editTextNumber.getText().toString());
            xmlSerializer.endTag(null, "ContactNo");
            xmlSerializer.startTag(null,"Email");
            xmlSerializer.text(employe.getEmail());
            xmlSerializer.endTag(null, "Email");
            xmlSerializer.startTag(null,"MilitaryRank");
            xmlSerializer.text(editTextRank.getText().toString());
            xmlSerializer.endTag(null, "MilitaryRank");
            xmlSerializer.startTag(null,"MilitaryUnit");
            xmlSerializer.text(editTextUnit.getText().toString());
            xmlSerializer.endTag(null, "MilitaryUnit");
            xmlSerializer.startTag(null,"UnitOfMilitaryUnit");
            xmlSerializer.text(employe.getUnitofMilitaryUnit());
            xmlSerializer.endTag(null, "UnitOfMilitaryUnit");
            xmlSerializer.startTag(null,"Adress");
            xmlSerializer.text(employe.getAdress());
            xmlSerializer.endTag(null, "Adress");
            xmlSerializer.startTag(null,"EmailToSend");
            xmlSerializer.text(textViewSendEmail.getText().toString());
            xmlSerializer.endTag(null, "EmailToSend");
            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
//            Toast.makeText(this, dataWrite, Toast.LENGTH_LONG).show();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void processParsing_userData(XmlPullParser parser, Employe employe) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();

        while(eventType != XmlPullParser.END_DOCUMENT)
        {
            String eltName = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("FirstName".equals(eltName))
                        employe.setFirstName(parser.nextText());
                    else if ("LastName".equals(eltName))
                        employe.setLastName(parser.nextText());
                    else if ("ContactNo".equals(eltName))
                        employe.setContactNo(parser.nextText());
                    else if ("Email".equals(eltName))
                        employe.setEmail(parser.nextText());
                    else if ("MilitaryRank".equals(eltName))
                        employe.setMilitaryRank(parser.nextText());
                    else if ("MilitaryUnit".equals(eltName))
                        employe.setMilitaryUnit(parser.nextText());
                    else if ("UnitOfMilitaryUnit".equals(eltName))
                        employe.setUnitofMilitaryUnit(parser.nextText());
                    else if ("Adress".equals(eltName))
                        employe.setAdress(parser.nextText());
                    else if ("EmailToSend".equals(eltName))
                        employe.setEmailToSend(parser.nextText());
                    break;
            }
            eventType = parser.next();
        }
    }
}
