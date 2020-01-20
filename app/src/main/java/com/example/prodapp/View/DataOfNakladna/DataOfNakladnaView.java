package com.example.prodapp.View.DataOfNakladna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prodapp.Model.DBHelper;
import com.example.prodapp.Model.DataOfNakladna.SummaryTotal;
import com.example.prodapp.Model.Employe.Employe;
import com.example.prodapp.Model.InfoOfNakladna.DBInfoOfNakladna;
import com.example.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.Presenter.DataOfNakladna.DataOfNakladnaPresenter;
import com.example.prodapp.Presenter.DataOfNakladna.IDataOfNakladnaPresenter;
import com.example.prodapp.R;
import com.example.prodapp.View.ChooseProduct.ChooseProductView;
import com.example.prodapp.View.ImageViewer.ImageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class DataOfNakladnaView extends AppCompatActivity implements AdapterCreateDataView.OnItemListener,  IDataOfNakladnaView {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    public static RecyclerView view;

    public AdapterCreateDataView adapter = null;
    public static DBHelper dbHelper;
    public static ArrayList<Uri> list_uri_img = new ArrayList<Uri>();
    String folder_uri_string;
    Uri image_uri;
    Uri file_uri;
    static int kod_poriadok = 1;

    boolean camera = false;
    static public boolean saveFile = false;
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

        total = findViewById(R.id.textTotal);
        buttonadd = findViewById(R.id.buttonAdd);
        view = findViewById(R.id.recycleid);
        fab = findViewById(R.id.fab);

        iDataOfNakladnaPresenter = new DataOfNakladnaPresenter(this);
        dbHelper = new DBHelper(this);

        if (savedInstanceState != null)
        {
            iDataOfNakladnaPresenter.setList(dbHelper.readData());
//            adapter = new AdapterCreateDataView(DataOfNakladnaView.this, dbHelper.readData(), this);
        }
        else
        {
            dbHelper.restartData();
        }

        view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCreateDataView(DataOfNakladnaView.this, iDataOfNakladnaPresenter.getList(), this);
//        if (savedInstanceState == null)
//            adapter = new AdapterCreateDataView(DataOfNakladnaView.this, iDataOfNakladnaPresenter.getList(), this);
        view.setAdapter(adapter);
        iDataOfNakladnaPresenter.onSummary();

        verifyStoragePermission();
        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate()");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("Error add element: ", "Enter");
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
                photoNakladna();
            }
        });

    }

//    @Override
//    protected void onRestoreInstanceState(Bundle state){
//        super.onRestoreInstanceState(state);
////        name_array.addAll(state.getStringArrayList("key"));
//        setListAdapter(arrayAdapter);
//        arrayAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
////        outState.putStringArrayList("key",name_array);
//    }

    //Вспливаюче вікно для фотографування основної накладної та надсилання сформованого файлу
    private void photoNakladna()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alert = builder.create();
        LayoutInflater inflater = DataOfNakladnaView.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.dialog_dataofnakladna, null);
//        alert.setTitle("Введіть інформацію про продукт\n" + list.get(position).getKod() + " " + list.get(position).getName());
        alert.setView(dialog_layout);

        final Button but_addphoto = dialog_layout.findViewById(R.id.buttonaddphoto);
        final Button but_sendphoto = dialog_layout.findViewById(R.id.but_sendphoto);

        but_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                but_sendphoto.setClickable(true);
                but_sendphoto.setBackgroundResource(R.drawable.fields_button_green);
                but_sendphoto.setVisibility(View.VISIBLE);
            }
        });

        but_sendphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (but_sendphoto.isClickable()) {
                    iDataOfNakladnaPresenter.onSave();
                    sendEmail();
                    alert.cancel();
                }
            }
        });


        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.send, menu);
        inflater.inflate(R.menu.save, menu);
//        inflater.inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_save:
//                iDataOfNakladnaPresenter.onSave();
////                saveFile = true;
//                Toast.makeText(this, "Файл збережений.", Toast.LENGTH_SHORT).show();
//                return true;
//
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean openCamera() {
        create_folder();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File img = new File(getFilesDir().getAbsolutePath() + "/"
                + name + "/" +  name + "_photo.png");

        image_uri = FileProvider.getUriForFile(
                DataOfNakladnaView.this,
                "com.example.prodapp", //(use your app signature + ".provider" )
                img);

        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
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
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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

    private void create_folder_photo_kod(String kod)
    {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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

    private void openCameraKod(String kod) {
        create_folder();
        create_folder_photo_kod(kod);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;
        String name_photo = kod.replaceAll("\\s", "") + "_photofolder";

        Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File img = null;
        img = new File(getFilesDir().getAbsolutePath() + "/"
                        + name + "/" + name_photo + "/" + kod.replaceAll("\\s", "")
                        + "_" + String.valueOf(kod_poriadok) + "_photo.png");
        kod_poriadok++;

            Uri temp = FileProvider.getUriForFile(
                    DataOfNakladnaView.this,
                    "com.example.prodapp", //(use your app signature + ".provider" )
                    img);


            list_uri_img.add(temp);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }


            camera.putExtra(MediaStore.EXTRA_OUTPUT, temp);
            if (camera.resolveActivity(getPackageManager()) != null)
                startActivityForResult(camera, 1);
    }

//    private void compress_photo(String photoPath)
//    {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        options.inSampleSize = 2;  //you can also calculate your inSampleSize
//        options.inJustDecodeBounds = false;
//        options.inTempStorage = new byte[16 * 1024];
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bmp= BitmapFactory.decodeFile(photoPath, options);
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(photoPath);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//
//    private void openCamera() {
//        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        String namePicture = InfoOfNakladnaPresenter.infoOfNakladna.getNameDogovir() + "_" + currentDate + "_photo";
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, namePicture);
//        contentValues.put(MediaStore.Images.Media.TITLE, namePicture);
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From camera");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(camera, IMAGE_CAPTURE_CODE);
////        Toast.makeText(this, "Фото збережено.", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
//                    openCamera();
                }
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnItemClick(int position) {
        iDataOfNakladnaPresenter.onEdit(position);
    }

    @Override
    public void OnCameraItemClick(String kod) {
        try {
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
    String name = DBtemp.getNameDogovir() + "+"
            + DBtemp.getNumberNakladna() + "+" + DBtemp.getDateNakladna().replace('/', '_')
            + "+" + currentDate;
//        String nameFile = name + "+file.xml";


    File folder = new File(getFilesDir().getAbsolutePath() +
            File.separator + name);
    boolean success = true;
    if (!folder.exists()) {
        success = folder.mkdirs();
    }
    openCameraKod(kod);
}
catch (Exception e) {
    Log.i("Error camera click: ", e.getMessage());
}
    }

    @Override
    public void OnViewPhoto(String kod) {
//        Toast.makeText(this, "Завантаження....", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DataOfNakladnaView.this, ImageActivity.class);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String name_folder = DBtemp.getNameDogovir() + "+"
                + DBtemp.getNumberNakladna() + "+" +  DBtemp.getDateNakladna().replace('/', '_')
                + "+" + currentDate;

        String name_photo = kod.replaceAll("\\s", "") + "_photofolder";


//        intent.putExtra("pathfolderall", name_folder);
//        intent.putExtra("pathfolderphoto", name_photo);
        correctPathForPhotoFolder = name_folder;
        correctPathForPhotoPhoto = name_photo;
//        Toast.makeText(this, name_folder, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, name_photo, Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }


    @Override
    public void pressSave(List<ProductsData> list) {
        if (list.size() > 0)
            writeFileXml(list);
        else
            Toast.makeText(this, "Файл не збережений. Список продуктів пустий", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pressSend() {
//        if (iDataOfNakladnaPresenter.getList().size() > 0 && saveFile == true)

        if (iDataOfNakladnaPresenter.getList().size() == 0)
            Toast.makeText(this, "Файл не надіслений. Список продуктів пустий", Toast.LENGTH_SHORT).show();
        else
            sendEmail();
//        else if (saveFile == false)
//            Toast.makeText(this, "Збережіть файл перед надсиланням.", Toast.LENGTH_SHORT).show();
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
//                saveFile = false;
                iDataOfNakladnaPresenter.onSummary();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    @Override
    public void pressBack(final List<ProductsData> list) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

//        if (saveFile == false && list.size() > 0)
//            alert.setMessage("Після виходу данні не будуть збережені.");
//        else
            alert.setMessage("Ви дійсно бажаєте вийти?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something with value!
                list.removeAll(list);
                DataOfNakladnaView.super.onBackPressed();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
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
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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

//            File img = new File(getFilesDir().getAbsolutePath() + "/"
//                    + name + "/" +  name + "_photo.png");
//
//            image_uri = FileProvider.getUriForFile(
//                    DataOfNakladnaView.this,
//                    "com.example.prodapp", //(use your app signature + ".provider" )
//                    img);

            File f = new File(folder, nameFile);
            folder_uri_string = folder.getAbsolutePath();
            file_uri = FileProvider.getUriForFile(DataOfNakladnaView.this, "com.example.prodapp", f);

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
                xmlSerializer.startTag(null, "Kol");
                xmlSerializer.text(String.valueOf(list.get(i).getKilbkistb()));
                xmlSerializer.endTag(null, "Kol");
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
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        InfoOfNakladna DBtemp = DBInfoOfNakladna.readData(this);
        String nameFile = DBtemp.getNameDogovir() + "_" + currentDate + "_file.xml";
        iDataOfNakladnaPresenter.onloadEmplInfo();
//        File f = new File(getFilesDir(),nameFile);
//        if (f.exists() && f.canRead()) {
        if (true) {
            File auxFile = new File(folder_uri_string, "readme");
//            Toast.makeText(this, auxFile.getPath(), Toast.LENGTH_LONG).show();
            try {
                FileOutputStream fileos = new FileOutputStream(auxFile);
                fileos.write(currentDate.getBytes());
//                Toast.makeText(this, "enter", Toast.LENGTH_SHORT).show();
                fileos.close();
//                Toast.makeText(this, auxFile.getPath(), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {}



            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("*/*");

//            ArrayList<Uri> list_uri = new ArrayList<Uri>();

//            Uri contentUri = FileProvider.getUriForFile(this, "com.example.prodapp", f);

            list_uri_img.add(image_uri);
            list_uri_img.add(file_uri);
//            list_uri = list_uri_img;

            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list_uri_img);

            String to[] = {iDataOfNakladnaPresenter.getEmploye().getEmailToSend()};
            String emplinfo =  iDataOfNakladnaPresenter.getEmploye().getMilitaryRank() + " " + iDataOfNakladnaPresenter.getEmploye().getLastName();
            sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Надходження товару від: " + emplinfo);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Оприбуткування продуктів харчування за договором: " + DBtemp.getNameDogovir() + "  " +currentDate);
            startActivity(Intent.createChooser(sendIntent, "Email:"));
        } else {
            Toast.makeText(DataOfNakladnaView.this, "Error",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

//        Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.i("j", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

//        Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy()");
    }


}
