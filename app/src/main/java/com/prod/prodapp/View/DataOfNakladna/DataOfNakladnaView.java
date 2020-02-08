package com.prod.prodapp.View.DataOfNakladna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.prodapp.Model.DBHelper;
import com.prod.prodapp.Model.DataOfNakladna.SummaryTotal;
import com.prod.prodapp.Model.DriveServicesHelper;
import com.prod.prodapp.Model.Employe.DBEmloye;
import com.prod.prodapp.Model.Employe.Employe;
import com.prod.prodapp.Model.InfoOfNakladna.DBInfoOfNakladna;
import com.prod.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.prod.prodapp.Model.ProductsData;
import com.prod.prodapp.Model.ZipFiles;
import com.prod.prodapp.Presenter.DataOfNakladna.DataOfNakladnaPresenter;
import com.prod.prodapp.Presenter.DataOfNakladna.IDataOfNakladnaPresenter;
import com.prod.prodapp.R;
import com.prod.prodapp.View.ChooseProduct.ChooseProductView;
import com.prod.prodapp.View.ImageViewer.ImageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;


public class DataOfNakladnaView extends AppCompatActivity implements AdapterCreateDataView.OnItemListener,  IDataOfNakladnaView {

    public static RecyclerView view;

    public static AdapterCreateDataView adapter = null;
    public static DBHelper dbHelper;
    public static ArrayList<Uri> list_uri_img = new ArrayList<Uri>();

    public static DriveServicesHelper driveServicesHelper = null;
    public static String currentDateFinal = null;

    String folder_uri_string;
    Uri image_uri;
    Uri file_uri;
    static String get_path = null;
    static public String correctPathForPhotoFolder = "";
    static public String correctPathForPhotoPhoto = "";

    private static final String TAG = "DataOfNakladnaView";

    TextView total;
    public Button buttonadd;
    FloatingActionButton fab;

    public static IDataOfNakladnaPresenter iDataOfNakladnaPresenter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_of_nakladna);


//        if (android.os.Build.VERSION.SDK_INT > 9)
//        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
//        }

        total = findViewById(R.id.textTotal);
        buttonadd = findViewById(R.id.buttonAdd);
        view = findViewById(R.id.recycleid);
        fab = findViewById(R.id.fab);

        iDataOfNakladnaPresenter = new DataOfNakladnaPresenter(this);
        dbHelper = new DBHelper(this);

        requestSignIn();

        if (savedInstanceState != null)
        {
            iDataOfNakladnaPresenter.setList(dbHelper.readData());
        }
        else
        {
            dbHelper.restartData();
            iDataOfNakladnaPresenter.setList(dbHelper.readData());
            try {
                get_path = getIntent().getExtras().getString("path_folder");
            }
            catch (Exception e)
            {
                Log.i("MyError ", e.getMessage());
            }
            if (get_path != null && !(get_path.equals("stop"))&& get_path.length() > 0)
            {
                try {
                    uploadFromArkhiv(get_path);
                }
                catch (Exception e)
                {
                    Log.e("MyError ", e.getMessage());
                }

            }
        }

        view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCreateDataView(DataOfNakladnaView.this, iDataOfNakladnaPresenter.getList(), this);
        view.setAdapter(adapter);
        iDataOfNakladnaPresenter.onSummary();

        verifyStoragePermission();
        Log.i(TAG, "onCreate()");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(DataOfNakladnaView.this, ChooseProductView.class);
                    startActivity(myIntent);
                }
                catch (Exception e)
                {
                    Log.i("Error add element: ", e.getMessage());

                }

            }
        });



        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(DataOfNakladnaView.this, R.anim.fadein);
                buttonadd.startAnimation(animation);
                photoNakladna();
            }
        });

    }

    private void uploadFromArkhiv(String path)
    {
        File directory = new File(getFilesDir().getAbsolutePath() + "/" + path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].getName().contains(".xml"))
                {
                    String temp [] = path.split("\\+");
                    if (temp.length > 2)
                        currentDateFinal = temp[3];
                    readUploadFile(files[i], temp[3]);
                }
            }

        }
    }

    private void readUploadFile(File file, String currentDate) {
        ParserXML(file.getAbsolutePath(), currentDate);
    }

    protected void ParserXML(String path_xml, String currentDate)
    {
        XmlPullParserFactory parserFactory;
        File file = new File(path_xml);
        try
        {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            InputStream is = new FileInputStream(file.getPath());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,null);

            processParsing(parser, currentDate);

        }
        catch (XmlPullParserException e) {e.getMessage();}
        catch (IOException e) {e.printStackTrace();}
    }

    private void processParsing(XmlPullParser parser, String currentDate) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Info")) {
                if (readInfo(parser, currentDate) == null)
                    Log.e("MyError", "Null Info");
            } else if (name.equals("Specification")) {

                List<ProductsData> list = readlistproduct(parser);

                if ( list != null)
                {
                    iDataOfNakladnaPresenter.setList(list);
                    dbHelper.restartData();
                    dbHelper.insertContact(list);
                }
                else
                    Log.e("MyError", "List in presenter is null");
            }
            }
    }

    private InfoOfNakladna readInfo(XmlPullParser parser, String currentDate) throws IOException, XmlPullParserException {

        int eventType = parser.getEventType();
        InfoOfNakladna infoOfNakladna = new InfoOfNakladna();



        DBInfoOfNakladna dbInfoOfNakladna = new DBInfoOfNakladna(this);
        dbInfoOfNakladna.restartDataInf();

        Log.e("MyError", "Enter2");

        while(eventType != XmlPullParser.END_DOCUMENT)
        {
            String eltName = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("Dog".equals(eltName)) {
                        infoOfNakladna.setNameDogovir(parser.nextText());

                    }
                    else if ("Nom".equals(eltName))
                        infoOfNakladna.setNumberNakladna(parser.nextText());
                    else if ("Data".equals(eltName))
                        infoOfNakladna.setDateNakladna(parser.nextText());
                    else if ("Avto".equals(eltName))
                        infoOfNakladna.setMarkaAvto(parser.nextText());
                    else if ("NomAvto".equals(eltName))
                        infoOfNakladna.setNomerAvto(parser.nextText());
                    else if ("Driver".equals(eltName))
                        infoOfNakladna.setNameDriver(parser.nextText());
                    else if ("KEKV".equals(eltName))
                        infoOfNakladna.setKEKV(parser.nextText());
                    else if ("Type".equals(eltName))
                        infoOfNakladna.setTypePostach(parser.nextText());
                    else if ("Plomba".equals(eltName))
                        infoOfNakladna.setNomerPlombi(parser.nextText());
                    break;
                case XmlPullParser.END_TAG:
                    eltName = parser.getName();

                    if ("Info".equals(eltName))
                    {
                        Log.e("MyError", infoOfNakladna.getNameDogovir());
                        dbInfoOfNakladna.insertInfo(infoOfNakladna);
                        DBInfoOfNakladna.insertCurrentDate(this, currentDate);
                        return infoOfNakladna;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return null;
    }

    private List<ProductsData> readlistproduct(XmlPullParser parser) throws IOException, XmlPullParserException{
        int eventType = parser.getEventType();
        List<ProductsData> list = new ArrayList<>();
        ProductsData productsData = null;

        while(eventType != XmlPullParser.END_DOCUMENT)
        {
            String eltName = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("Product".equals(eltName))
                        productsData = new ProductsData();
                    else if ("Kod".equals(eltName)) {
                        if (productsData != null)
                            productsData.setKod(parser.nextText());
                    }
                    else if ("KodNam".equals(eltName)) {
                        if (productsData != null)
                            productsData.setName(parser.nextText());
                    }
                    else if ("Kol".equals(eltName)) {
                            if (productsData != null)
                                productsData.setKilbkistb(Double.parseDouble(parser.nextText()));
                        }
                    else if ("Price".equals(eltName)) {
                        if (productsData != null)
                            productsData.setPrice(Double.parseDouble(parser.nextText()));
                    }
                    else if ("DanaZ".equals(eltName)) {
                                if (productsData != null)
                                    productsData.setDataStart(parser.nextText());
                            }
                    else if ("DataP".equals(eltName)) {
                                    if (productsData != null)
                                        productsData.setDataFinish(parser.nextText());
                                }
                    else if ("Termin".equals(eltName)) {
                                        if (productsData != null)
                                            productsData.setDataTrival(parser.nextText());
                                    }
                    break;

                case XmlPullParser.END_TAG:
                    eltName = parser.getName();

                    if ("Specification".equals(eltName))
                    {
                        return list;
                    }
                    else if ("Product".equals(eltName)) {
                        if (productsData != null) {
                            list.add(productsData);
                            productsData = null;
                        }
                    }
                    break;
            }
;
            eventType = parser.next();
        }
        return null;
    }

    private void requestSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        startActivityForResult(client.getSignInIntent(), 400);
    }

    //Вспливаюче вікно для фотографування основної накладної та надсилання сформованого файлу
    private void photoNakladna()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alert = builder.create();
        LayoutInflater inflater = DataOfNakladnaView.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.dialog_dataofnakladna, null);
        alert.setView(dialog_layout);

        final Button but_addphoto = dialog_layout.findViewById(R.id.buttonaddphoto);
        final Button but_sendphoto = dialog_layout.findViewById(R.id.but_sendphoto);

        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String folder = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        String photo_folder = folder + "_photo.jpg";

        File photo_file = new File(getFilesDir().getAbsolutePath() +
                File.separator + folder + File.separator + photo_folder);
        if (photo_file.exists())
        {
            but_sendphoto.setClickable(true);
            but_sendphoto.setBackgroundResource(R.drawable.fields_button_green);
            but_sendphoto.setVisibility(View.VISIBLE);
        }



        but_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(DataOfNakladnaView.this, R.anim.fadein);
                but_addphoto.startAnimation(animation);
                openCamera();
                but_sendphoto.setClickable(true);
                but_sendphoto.setBackgroundResource(R.drawable.fields_button_green);
                but_sendphoto.setVisibility(View.VISIBLE);
            }
        });

        but_sendphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(DataOfNakladnaView.this, R.anim.fadein);
                but_sendphoto.startAnimation(animation);
//                if (but_sendphoto.isClickable()) {
//                    alert.cancel();
                    iDataOfNakladnaPresenter.onSave();
                    pressSend();
                    alert.cancel();
//                }
            }
        });


        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_save:
                iDataOfNakladnaPresenter.onSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean openCamera() {
        create_folder();
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File img = new File(getFilesDir().getAbsolutePath() + "/"
                + name + "/" +  name + "_photo.jpg");

        image_uri = FileProvider.getUriForFile(
                DataOfNakladnaView.this,
                "com.prod.prodapp", img);

        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        if (camera.resolveActivity(getPackageManager()) != null)
            startActivityForResult(camera, 1);
        return (true);
    }

    private void verifyStoragePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 1000);
            }
        }
    }

    private void create_folder()
    {
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;

        File folder = new File(getFilesDir().getAbsolutePath() +
                File.separator + name);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    private String create_folder_name()
    {
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        return (name);
    }

    private void create_folder_photo_kod(String kod)
    {
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;

        String name_photo = kod.replaceAll("\\s", "") + "_photofolder";

        File folder = new File(getFilesDir().getAbsolutePath() +
                File.separator + name + File.separator + name_photo);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    private void openCameraKod(String kod, int i) {
        create_folder();
        create_folder_photo_kod(kod);
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        ProductsData productsData = DBHelper.readDataStat(this).get(i);
        int number = Integer.parseInt(productsData.getNumberphoto());
        Log.i("Message", productsData.getKod() + " " + number);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        String name_photo = kod.replaceAll("\\s", "") + "_photofolder";

        Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        File img = null;
        img = new File(getFilesDir().getAbsolutePath() + "/"
                        + name + "/" + name_photo + "/" + kod.replaceAll("\\s", "")
                        + "_" + String.valueOf(number) + "_photo.jpg");

        Log.i("Message", productsData.getKod() + " " + number + " " + productsData.getNumberphoto());

        dbHelper.updateDataStat(String.valueOf(i + 1), productsData);
        iDataOfNakladnaPresenter.setList(dbHelper.readData());



            Uri temp = FileProvider.getUriForFile(
                    DataOfNakladnaView.this,
                    "com.prod.prodapp", //(use your app signature + ".provider" )
                    img);


            list_uri_img.add(temp);

            camera.putExtra(MediaStore.EXTRA_OUTPUT, temp);
            if (camera.resolveActivity(getPackageManager()) != null)
                startActivityForResult(camera, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                    Toast.makeText(this, "Доступ заборонений", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 400) {
            Log.i("MyError", "requestcode 400");
            Log.i("MyError", String.valueOf(RESULT_OK));
        }

        switch (requestCode)
        {
            case 400:
                if (requestCode != RESULT_OK)
                {
                Log.i("MyError", "handle signed it");
                Toast.makeText(this, "Обліковий запис завантажений", Toast.LENGTH_SHORT).show();
                handleSignIntent(data);
                }
                break;
        }
    }

    private void handleSignIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(DataOfNakladnaView.this, Collections.singleton(DriveScopes.DRIVE_FILE));

                        credential.setSelectedAccount(googleSignInAccount.getAccount());

                        Drive googleDriveServices = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("ProdApp")
                                .build();

                        driveServicesHelper = new DriveServicesHelper(googleDriveServices);
                        toast_helper_success();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast_helper_fail(e.getMessage());
                    }
                });
    }


    public void toast_helper_fail(String error)
    {
        Toast.makeText(this, "Помилка при підключенні " + error, Toast.LENGTH_LONG).show();
        Log.i("MyError Listener Fail", error);
        File auxFile = new File(getFilesDir().getAbsolutePath(), "readme_err.txt");
        try {
            FileOutputStream fileos = new FileOutputStream(auxFile);
            fileos.write(error.getBytes());
            fileos.close();
        }
        catch (IOException e) {}
    }

    public void toast_helper_success()
    {
        Toast.makeText(this, "Підключення до Google Drive успішне", Toast.LENGTH_LONG).show();
        Log.i("MyError Listener ", "success");
    }

    public boolean uploadpdffile(String filepath, String name)
    {
        ProgressDialog progressDialog = new ProgressDialog(DataOfNakladnaView.this);
        progressDialog.setTitle("Uploading to Google Drive");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if (driveServicesHelper == null)
        {
            Log.i("MyError", "Helper is null");
            progressDialog.dismiss();
            Toast.makeText(this, "Helper is null", Toast.LENGTH_SHORT).show();
            DataOfNakladnaView.iDataOfNakladnaPresenter.onSendGmail();
            return (false);
        }

        if (driveServicesHelper != null) {
            driveServicesHelper.createFilePDF(filepath, name, this)
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            progressDialog.dismiss();
                            createFileReadme();

                            Toast.makeText(getApplicationContext(), "Завантаження файлу успішне", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Виникла помилка при завантаженні", Toast.LENGTH_LONG).show();
                            Log.i("MyError", e.getMessage());
                        }
                    });
        }
        return (true);
    }

    @Override
    public void OnItemClick(int position) {
        iDataOfNakladnaPresenter.onEdit(position);
    }

    @Override
    public void OnCameraItemClick(String kod, int i) {
        try {
            String currentDate = null;
            if (DBInfoOfNakladna.readCurrentDate(this) == null
                    || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            else
                currentDate = DBInfoOfNakladna.readCurrentDate(this);
            InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
            String name = DBtemp.getNameDogovir() + "+"
                    + DBtemp.getNumberNakladna() + "+" + DBtemp.getDateNakladna().replace('/', '_')
                    + "+" + currentDate;

            File folder = new File(getFilesDir().getAbsolutePath() +
            File.separator + name);
    boolean success = true;
    if (!folder.exists()) {
        success = folder.mkdirs();
    }
    openCameraKod(kod, i);
}
catch (Exception e) {
    Log.i("Error camera click: ", e.getMessage());
}
    }

    @Override
    public void OnViewPhoto(String kod) {
        Intent intent = new Intent(DataOfNakladnaView.this, ImageActivity.class);
        String currentDate = DBInfoOfNakladna.readCurrentDate(this);
        Log.i("MyError 1", currentDate);
        if (currentDate == null
                || currentDate.length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.i("MyError 2", currentDate);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name_folder = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;

        String name_photo = kod.replaceAll("\\s", "") + "_photofolder";

        correctPathForPhotoFolder = name_folder;
        correctPathForPhotoPhoto = name_photo;

        startActivity(intent);
    }


    @Override
    public void pressSendGmail() {
        sendEmailtoGmail();
    }

    @Override
    public void pressSave(List<ProductsData> list) {
        list = DBHelper.readDataStat(this);
        if (list.size() > 0) {
            writeFileXml(list);
            Toast.makeText(this, "Оприбуткування збережене", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Файл не збережений. Список продуктів пустий", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pressSend() {
        if (iDataOfNakladnaPresenter.getList().size() == 0)
            Toast.makeText(this, "Файл не надіслений. Список продуктів пустий", Toast.LENGTH_SHORT).show();
        else {
            String currentDate = null;
            if (DBInfoOfNakladna.readCurrentDate(this) == null
                    || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            else
                currentDate = DBInfoOfNakladna.readCurrentDate(this);
            InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
            String nameFile = DBtemp.getNameDogovir() + "_" + currentDate;
            sendEmail();
            uploadpdffile(folder_uri_string + ".zip", nameFile);
        }
    }

    @Override
    public void onBackPressed() {
        iDataOfNakladnaPresenter.onBack();
    }

    @Override
    public void pressEdit(final int position, final List<ProductsData> list) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Видалення товару");
        alert.setMessage("Видалити товар? ");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("Position: ", String.valueOf(position));
                dbHelper.deleteData(String.valueOf(position));
                remove_element_list(position, list);
                iDataOfNakladnaPresenter.onSummary();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    @Override
    public void pressBack(final List<ProductsData> list) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setMessage("Ви дійсно бажаєте вийти?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                list.removeAll(list);
                DataOfNakladnaView.super.onBackPressed();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    @Override
    public void pressLoadEmplInfo(Employe employe) {
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
    public void pressSummary(List<ProductsData> list) {
        if (list.size() > 0)
            buttonadd.setVisibility(View.VISIBLE);
        else
            buttonadd.setVisibility(View.INVISIBLE);
        total.setText(SummaryTotal.summaryList(list));
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

    private void remove_element_list(int position, List<ProductsData> list)
    {
        int counter = 0;

        Iterator<ProductsData> i = list.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (counter == position) {
                i.remove();
                this.adapter.notifyDataSetChanged();
                return;
            }
            counter++;
        }
    }


    private void writeFileXml(List<ProductsData> list) {
        try {
            String currentDate = null;
            if (DBInfoOfNakladna.readCurrentDate(this) == null
                    || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            else
                currentDate = DBInfoOfNakladna.readCurrentDate(this);
            InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
            String name = DBtemp.getNameDogovir() + "+"
                    + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                    + "+" + currentDate;
            String nameFile = name + "+file.xml";

            File folder = new File(getFilesDir().getAbsolutePath() +
                    File.separator + name);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            File f = new File(folder, nameFile);
            folder_uri_string = folder.getAbsolutePath();
            file_uri = FileProvider.getUriForFile(DataOfNakladnaView.this, "com.prod.prodapp", f);

            FileOutputStream fileos = new FileOutputStream(f);
//            FileOutputStream fileos= getApplicationContext().openFileOutput(f.getAbsolutePath(), Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "FFF");
            xmlSerializer.startTag(null, "Info");
            xmlSerializer.startTag(null, "Dog");
            xmlSerializer.text(DBtemp.getNameDogovir().replace("    ", ""));
            xmlSerializer.endTag(null, "Dog");
            xmlSerializer.startTag(null, "Nom");
            xmlSerializer.text(DBtemp.getNumberNakladna());
            xmlSerializer.endTag(null, "Nom");
            xmlSerializer.startTag(null,"Data");
            xmlSerializer.text(DBtemp.getDateNakladna());
            xmlSerializer.endTag(null, "Data");
            xmlSerializer.startTag(null,"Avto");
            xmlSerializer.text(DBtemp.getMarkaAvto());
            xmlSerializer.endTag(null, "Avto");
            xmlSerializer.startTag(null,"NomAvto");
            xmlSerializer.text(DBtemp.getNomerAvto());
            xmlSerializer.endTag(null, "NomAvto");
            xmlSerializer.startTag(null,"Driver");
            xmlSerializer.text(DBtemp.getNameDriver());
            xmlSerializer.endTag(null, "Driver");
            xmlSerializer.startTag(null,"KEKV");
            xmlSerializer.text(DBtemp.getKEKV());
            xmlSerializer.endTag(null, "KEKV");
            xmlSerializer.startTag(null,"Type");
            xmlSerializer.text(DBtemp.getTypePostach());
            xmlSerializer.endTag(null, "Type");
            xmlSerializer.startTag(null,"Plomba");
            xmlSerializer.text(DBtemp.getNomerPlombi());
            xmlSerializer.endTag(null, "Plomba");
            xmlSerializer.endTag(null, "Info");

            xmlSerializer.startTag(null,"Specification");
            for (int i = 0; i < list.size(); i++) {
                xmlSerializer.startTag(null, "Product");
                xmlSerializer.startTag(null, "Kod");
                xmlSerializer.text(list.get(i).getKod().replaceAll("\\D", ""));
                xmlSerializer.endTag(null, "Kod");
                xmlSerializer.startTag(null, "KodNam");
                xmlSerializer.text(String.valueOf(list.get(i).getName()));
                xmlSerializer.endTag(null, "KodNam");
                xmlSerializer.startTag(null, "Kol");
                xmlSerializer.text(String.valueOf(list.get(i).getKilbkistb()));
                xmlSerializer.endTag(null, "Kol");
                xmlSerializer.startTag(null, "Price");
                xmlSerializer.text(String.valueOf(list.get(i).getPrice()));
                xmlSerializer.endTag(null, "Price");
                xmlSerializer.startTag(null, "DanaZ");
                xmlSerializer.text(String.valueOf(list.get(i).getDataStart()));
                xmlSerializer.endTag(null, "DanaZ");
                xmlSerializer.startTag(null, "DataP");
                xmlSerializer.text(String.valueOf(list.get(i).getDataFinish()));
                xmlSerializer.endTag(null, "DataP");
                xmlSerializer.startTag(null, "Termin");
                xmlSerializer.text(String.valueOf(list.get(i).getDataTrival()));
                xmlSerializer.endTag(null, "Termin");
                xmlSerializer.endTag(null, "Product");
            }
            xmlSerializer.endTag(null, "Specification");
            xmlSerializer.endTag(null, "FFF");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
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

    private void sendEmail() {
//        String currentDate = null;
//        if (DBInfoOfNakladna.readCurrentDate(this) == null
//                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
//            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        else
//            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        iDataOfNakladnaPresenter.onloadEmplInfo();
        if (true) {
//            File auxFile = new File(folder_uri_string, "readme");
//            try {
//                FileOutputStream fileos = new FileOutputStream(auxFile);
//                fileos.write(currentDate.getBytes());
//                fileos.close();
//            }
//            catch (IOException e) {}


            ZipFiles zipFiles = new ZipFiles(folder_uri_string, folder_uri_string+".zip");
            zipFiles.zipDirectory();

            sendtoftpserver(folder_uri_string+".zip");



        } else {

        }
    }

    public void createFileReadme()
    {
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);

        File auxFile = new File(folder_uri_string, "readme");
        try {
            FileOutputStream fileos = new FileOutputStream(auxFile);
            fileos.write(currentDate.getBytes());
            fileos.close();
        }
        catch (IOException e) {}

    }

    public void sendtoftpserver(String path)
    {
        Thread thread = new Thread(new Runnable() {

            FTPClient con = null;
            boolean check = false;

            @Override
            public void run() {
                try  {
                    con = new FTPClient();
                    con.connect("gu-zsu-prod.cc.ua", 21);
                    con.login("w_gu-zsu-prod-cc-ua_77a485be", "aed4846a678");
                    con.setType(FTPClient.TYPE_BINARY);

                    String remoteFilePath = "/prodapp";
                    FTPFile ftpFile [] = con.list(remoteFilePath);
                    if (ftpFile != null) {
                        for (int i = 0; i < ftpFile.length; i++)
                        {
                            Log.i("Specifi",ftpFile[i].getName() );
                            if (ftpFile[i].getName().equals(DBEmloye.readData(DataOfNakladnaView.this).getMilitaryUnit())) {
                                check = true;
                                Log.i("Specifi", "TRUE");
                            }
                        }
                    } else {

                        System.out.println("The specified file/directory may not exist!");
                        Log.i("Specifi","The specified file/directory may not exist!" );
                    }

                    if (check == false) {
                        con.createDirectory("/prodapp/" + DBEmloye.readData(DataOfNakladnaView.this).getMilitaryUnit());
                    }
                    con.changeDirectory("/prodapp/" + DBEmloye.readData(DataOfNakladnaView.this).getMilitaryUnit());
                    con.upload(new File(path));
                    con.disconnect(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


//        FTPClient con = null;
//        try
//        {
//            con = new FTPClient();
//            con.connect("gu-zsu-prod.cc.ua", 21);
//            con.login("w_gu-zsu-prod-cc-ua_77a485be", "aed4846a678");
//            con.setType(FTPClient.TYPE_BINARY);
//            con.changeDirectory("/prodapp");
//            con.upload(new File(path));
//            con.disconnect(true);
////            con = new FTPClient();
////            con.connect("prod.zzz.com.ua", 21);
////            con.login("prodappuser", "Prodappuser777");
////            con.setType(FTPClient.TYPE_BINARY);
////            con.changeDirectory("/prod.zzz.com.ua/prodapp");
////            con.upload(new File(path));
////            con.disconnect(true);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            Log.i("Hosting", "Error " + e.getMessage());
//        }

    }

    public void sendEmailtoGmail()
    {
        String currentDate = null;
        if (DBInfoOfNakladna.readCurrentDate(this) == null
                || DBInfoOfNakladna.readCurrentDate(this).length() < 3)
            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        else
            currentDate = DBInfoOfNakladna.readCurrentDate(this);
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);

        File tempfile = new File(folder_uri_string+".zip");
        try {
            if (tempfile.createNewFile())
                Log.i("Error ", "file error created");
            else
                Log.i("Error", "File is created");
        }
        catch (Exception e)
        {
            Log.i("Error", e.getMessage());
        }
        ArrayList<Uri> senduri = new ArrayList<>();

        Uri uri = FileProvider.getUriForFile(
                DataOfNakladnaView.this,
                "com.prod.prodapp",
                tempfile);

        File products = new File(folder_uri_string + File.separator
                + create_folder_name() + "+file.xml");
        File photo_products = new File(folder_uri_string + File.separator
                + create_folder_name() + "_photo.jpg");

        Log.i("MyErrornameProduct", products.getAbsolutePath());
        Log.i("MyErrornamePhotoProduct", photo_products.getAbsolutePath());

        Uri uri1 = FileProvider.getUriForFile(
                DataOfNakladnaView.this,
                "com.prod.prodapp",
                products);

        Uri uri2 = FileProvider.getUriForFile(
                DataOfNakladnaView.this,
                "com.prod.prodapp",
                photo_products);
        if (products.exists())
            senduri.add(uri1);
        if (photo_products.exists())
            senduri.add(uri2);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("*/*");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, senduri);
        String to[] = {iDataOfNakladnaPresenter.getEmploye().getEmailToSend()};
        String emplinfo = iDataOfNakladnaPresenter.getEmploye().getMilitaryRank() + " " + iDataOfNakladnaPresenter.getEmploye().getLastName();
        sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Надходження товару від: " + emplinfo);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Оприбуткування продуктів харчування за договором: " + DBtemp.getNameDogovir()
                + "  " + currentDate + "\n" + "https://drive.google.com/open?id=" + DBInfoOfNakladna.readStringId(this));
        startActivity(Intent.createChooser(sendIntent, "Email:"));

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        view.setAdapter(adapter);

        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("j", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }


}
