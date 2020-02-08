package com.prod.prodapp.View.InfoOfNakladna;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.prodapp.Model.InfoOfNakladna.DBInfoOfNakladna;
import com.prod.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.prod.prodapp.Presenter.InfoOfNakladna.IInfoOfNakladnaPresenter;
import com.prod.prodapp.Presenter.InfoOfNakladna.InfoOfNakladnaPresenter;
import com.prod.prodapp.R;
import com.prod.prodapp.View.ChoiseMenu.ChoiseMenuView;
import com.prod.prodapp.View.DataOfNakladna.DataOfNakladnaView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InfoOfNakladnaProdView extends AppCompatActivity implements IInfoOfNakladnaView {

    IInfoOfNakladnaPresenter iInfoOfNakladnaPresenter;

    Spinner spinnerDogovir;
    Spinner spinnerKekv;
    Spinner spinnerType;
    EditText editTextnumbernakladna;
    TextView textViewDate;
    EditText editTextMarkaAvto;
    EditText editTextNomerZnak;
    EditText editTextVodii;
    EditText editTextPlomba;
    Button butaddinfo;

    Calendar dateCalendar;

    DBInfoOfNakladna dbInfoOfNakladna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_nakladna_prod);

        iInfoOfNakladnaPresenter = new InfoOfNakladnaPresenter(this);

        spinnerDogovir = findViewById(R.id.spinnerdogovir);
        spinnerKekv = findViewById(R.id.spinnerkekv);
        spinnerType = findViewById(R.id.spinnertype);
        editTextnumbernakladna = findViewById(R.id.editTextnumber_nakladna);
        textViewDate = findViewById(R.id.textViewDate);
        editTextMarkaAvto = findViewById(R.id.editTextMarkaAvto);
        editTextNomerZnak = findViewById(R.id.editTextNomerZnak);
        editTextVodii = findViewById(R.id.editTextVodii);
        editTextPlomba = findViewById(R.id.editTextPlomba);
        butaddinfo = findViewById(R.id.butInfoNakladna);

        dateCalendar = Calendar.getInstance();

        dbInfoOfNakladna = new DBInfoOfNakladna(this);

        verifyStoragePermission();

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.dogovora, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDogovir.setAdapter(adapter);

        ArrayAdapter<?> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.KEKV, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKekv.setAdapter(adapter2);

        ArrayAdapter<?> adapter3 =
                ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter3);

        spinnerDogovir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iInfoOfNakladnaPresenter.onDate();
            }
        });

        butaddinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iInfoOfNakladnaPresenter.onSaveInfo())
                {
                    Intent intent = new Intent(InfoOfNakladnaProdView.this, DataOfNakladnaView.class);
                    intent.putExtra("path_folder", "stop");
                    startActivity(intent);

                }

            }
        });

        if (ChoiseMenuView.qrCodeParser != null)
        {
//            iInfoOfNakladnaPresenter.setInfoOfNakladna(ChoiseMenuView.qrCodeParser.getInfoOfNakladna());
            InfoOfNakladna temp = ChoiseMenuView.qrCodeParser.getInfoOfNakladna();
            spinnerDogovir.setSelection(0);
            textViewDate.setText(temp.getDateNakladna());
            editTextMarkaAvto.setText(temp.getMarkaAvto());
            editTextVodii.setText(temp.getNameDriver());
            editTextNomerZnak.setText(temp.getNomerAvto());
            editTextnumbernakladna.setText(temp.getNumberNakladna());
        }
    }

    private void verifyStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.GET_ACCOUNTS};
                requestPermissions(permission, 1000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.check, menu);
        return true;
    }

    @Override
    public boolean pressSaveInfo(InfoOfNakladna infoOfNakladna) {
        infoOfNakladna.setNameDogovir(spinnerDogovir.getSelectedItem().toString());
        infoOfNakladna.setDateNakladna(textViewDate.getText().toString().replace("/", "_"));
        infoOfNakladna.setMarkaAvto(editTextMarkaAvto.getText().toString().replace("/", "_"));
        infoOfNakladna.setNameDriver(editTextVodii.getText().toString().replace("/", "_"));
        infoOfNakladna.setNomerAvto(editTextNomerZnak.getText().toString().replace("/", "_"));
        infoOfNakladna.setNumberNakladna(editTextnumbernakladna.getText().toString().replace("/", "_"));
        infoOfNakladna.setKEKV(spinnerKekv.getSelectedItem().toString().replace("/", "_"));
        infoOfNakladna.setTypePostach(spinnerType.getSelectedItem().toString().replace("/", "_"));
        infoOfNakladna.setNomerPlombi(editTextPlomba.getText().toString().replace("/", "_"));
        infoOfNakladna.setCreateDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        String err = infoOfNakladna.checkInfoClass();
        if (!("Помилка:\n".equals(err))){
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            return (false);
        }
        else {
            dbInfoOfNakladna.restartDataInf();
            dbInfoOfNakladna.insertInfo(infoOfNakladna);
            Toast.makeText(this, "Інформація введена коректно.", Toast.LENGTH_SHORT).show();
        }
        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_check:
                if (iInfoOfNakladnaPresenter.onSaveInfo())
                {
                    Intent intent = new Intent(InfoOfNakladnaProdView.this, DataOfNakladnaView.class);
                    startActivity(intent);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void pressChooseDogovir() {

    }

    @Override
    public void pressTypeNumber() {

    }

    @Override
    public void pressDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        new DatePickerDialog(InfoOfNakladnaProdView.this, date, dateCalendar
                .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textViewDate.setText(sdf.format(dateCalendar.getTime()));
    }

    @Override
    public void pressMarkaAvto() {

    }

    @Override
    public void pressNomerZnak() {

    }

    @Override
    public void pressNameDriver() {

    }

    @Override
    public void errorMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
