package com.prod.prodapp.View.Register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.prodapp.Model.Employe.DBEmloye;
import com.prod.prodapp.Model.Employe.Employe;
import com.prod.prodapp.Presenter.Register.IRegisterPresenter;
import com.prod.prodapp.Presenter.Register.RegisterPresenter;
import com.prod.prodapp.R;
import com.prod.prodapp.View.ChoiseMenu.ChoiseMenuView;
import com.prod.prodapp.View.SplashActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 10;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    TextView txt_path;
    Intent myFileIntent;
    Button butFoundPath;
    Button butDownoload;

    IRegisterPresenter registerPresenter;

    DBEmloye dbEmloye;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbEmloye = new DBEmloye(this);

        butFoundPath = findViewById(R.id.buttonFindPath);
        butDownoload = findViewById(R.id.buttonDownoload);
        txt_path = findViewById(R.id.text_path);

        registerPresenter = new RegisterPresenter(this);
        registerPresenter.onCheckExistFile();



        verifyStoragePermissions(this);


        butFoundPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPresenter.onFindPath();
            }
        });

        butDownoload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPresenter.onDownoload(txt_path.getText().toString());
            }
        });
    }

    @Override
    public void onRegisterResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkFileExist(Employe employe) {
        File temp_file = new File(getFilesDir(),"data_user.xml");
        if (temp_file.exists()) {
//            writeFileXml(employe); //delete
            readFileXml(employe);

            if (employe != null && employe.check_class()) {
//                Intent intent = new Intent(RegisterActivity.this, ImageActivity.class);
                dbEmloye.restartData();
                dbEmloye.insertContact(employe);
                Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(this, "Не коректний загрузочний файл.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void readFileXml(Employe employe) {
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
    public void processParsing_userData(XmlPullParser parser, Employe employe) throws IOException, XmlPullParserException {
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

    @Override
    public void pressFindPath() {
        myFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        myFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        myFileIntent.setType("*/*");
        startActivityForResult(myFileIntent, READ_REQUEST_CODE);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
//                File f = getExternalFilesDir("file.xml");
//                path = f.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }

                txt_path.setText(path);
                ParserXML(path, registerPresenter.getEmploye());
            }
        }
    }

    protected void ParserXML(String path_xml, Employe employe)
    {
        XmlPullParserFactory parserFactory;
        File file = new File(Environment.getExternalStorageDirectory(), path_xml);
        verifyStoragePermissions(this);
        try
        {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            verifyStoragePermissions(this);

            InputStream is = new FileInputStream(file.getPath());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,null);

            processParsing(parser, employe);

        }
        catch (XmlPullParserException e) {e.getMessage();}
        catch (IOException e) {e.printStackTrace();}
    }

    private void processParsing(XmlPullParser parser, Employe employe) throws IOException, XmlPullParserException
    {
        int eventType = parser.getEventType();
//        employe = new Employe();

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
    @Override
    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
    public void pressDownoload(Employe employe) {
        if (employe != null) {
            if (employe.check_class()) {
                Toast.makeText(this, "Файл коректний та загружений.", Toast.LENGTH_LONG).show();
                writeFileXml(employe);
                Intent intent = new Intent(RegisterActivity.this, ChoiseMenuView.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Файл не коректний спробуйте інший файл.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void writeFileXml(Employe employe) {
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
            xmlSerializer.text(employe.getContactNo());
            xmlSerializer.endTag(null, "ContactNo");
            xmlSerializer.startTag(null,"Email");
            xmlSerializer.text(employe.getEmail());
            xmlSerializer.endTag(null, "Email");
            xmlSerializer.startTag(null,"MilitaryRank");
            xmlSerializer.text(employe.getMilitaryRank());
            xmlSerializer.endTag(null, "MilitaryRank");
            xmlSerializer.startTag(null,"MilitaryUnit");
            xmlSerializer.text(employe.getMilitaryUnit());
            xmlSerializer.endTag(null, "MilitaryUnit");
            xmlSerializer.startTag(null,"UnitOfMilitaryUnit");
            xmlSerializer.text(employe.getUnitofMilitaryUnit());
            xmlSerializer.endTag(null, "UnitOfMilitaryUnit");
            xmlSerializer.startTag(null,"Adress");
            xmlSerializer.text(employe.getAdress());
            xmlSerializer.endTag(null, "Adress");
            xmlSerializer.startTag(null,"EmailToSend");
            xmlSerializer.text(employe.getEmailToSend());
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


}
