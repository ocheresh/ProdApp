package com.example.prodapp.View.ChooseProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prodapp.Model.ChooseProduct.HandlerXMLParser;
import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.Presenter.ChooseProduct.ChooseProductPresenter;
import com.example.prodapp.Presenter.ChooseProduct.IChooseProductPresenter;
import com.example.prodapp.Presenter.DataOfNakladna.DataOfNakladnaPresenter;
import com.example.prodapp.Presenter.InfoOfNakladna.InfoOfNakladnaPresenter;
import com.example.prodapp.R;
import com.example.prodapp.View.DataOfNakladna.DataOfNakladnaView;
import com.example.prodapp.View.InfoOfNakladna.InfoOfNakladnaProdView;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ChooseProductView extends AppCompatActivity implements IChooseProductView, AdapterChooseProduct.OnItemListener{

    RecyclerView recyclerView;
    AdapterChooseProduct myAdapter;
    public static List<ProductsData> list = null;
    List<ProductsData> newlist = new ArrayList<>();

    IChooseProductPresenter iChooseProductPresenter;
    Calendar dateCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product_view);

        iChooseProductPresenter = new ChooseProductPresenter(this);

        if (list == null) {

            iChooseProductPresenter.onReadData();
        }

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new AdapterChooseProduct(ChooseProductView.this, list, this);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void OnItemClick(final int position) {
        iChooseProductPresenter.onAddElement(position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (myAdapter != null){
                    myAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    private void read_data_xml()
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            String name = InfoOfNakladnaPresenter.infoOfNakladna.getNameDogovir() + ".xml";
            InputStream is = getAssets().open(name);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            HandlerXMLParser handler = new HandlerXMLParser();
            saxParser.parse(is, handler);
            //Get Employees list
            list = handler.getEmpList();
            //print employee information
            for(ProductsData emp : list)
                System.out.println(emp);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    public void pressReadData() {
        read_data_xml();
    }

    @Override
    public void pressAddElement(final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = ChooseProductView.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.alertdialog, null);
        alert.setTitle("Введіть інформацію про продукт:");
        alert.setView(dialog_layout);

        final TextView tt1 = dialog_layout.findViewById(R.id.tt1);
        final TextView tt2 = dialog_layout.findViewById(R.id.tt2);
        final TextView tt3 = dialog_layout.findViewById(R.id.tt3);
        final TextView tt4 = dialog_layout.findViewById(R.id.tt4);

        final EditText getKilkist = (EditText)dialog_layout.findViewById(R.id.editKilkist);
        final EditText getAllDate = (EditText)dialog_layout.findViewById(R.id.editDateVikorist);
        final EditText getDateStart = dialog_layout.findViewById(R.id.editDateStart);
        final EditText getDateFinish = (EditText)dialog_layout.findViewById(R.id.editDateEnd);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        getDateStart.setText(currentDate);
        setMyText(tt2);

        getKilkist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setMyText(tt1);
            }
        });

        getDateStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setMyText(tt2);
            }
        });

        getDateFinish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setMyText(tt3);
            }
        });

        getAllDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setMyText(tt4);
            }
        });

        getDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateCalendar.set(Calendar.YEAR, year);
                        dateCalendar.set(Calendar.MONTH, monthOfYear);
                        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        if (checkValueDate(sdf.format(dateCalendar.getTime()), getDateFinish.getText().toString())) {
                            getDateStart.setText(sdf.format(dateCalendar.getTime()));
                            setCorrectDateFin(getDateStart, getDateFinish, getAllDate);
                        }
                        else
                            Toast.makeText(ChooseProductView.this, "Не коректна початкова дата використання", Toast.LENGTH_SHORT).show();
                    }

                };

                new DatePickerDialog(ChooseProductView.this, date, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        getDateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDateStart.callOnClick();
                }
            }
        });

        getDateFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateCalendar.set(Calendar.YEAR, year);
                        dateCalendar.set(Calendar.MONTH, monthOfYear);
                        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        if (checkValueDate(getDateStart.getText().toString(), sdf.format(dateCalendar.getTime()))) {
                            getDateFinish.setText(sdf.format(dateCalendar.getTime()));
                            setCorrectDateFin(getDateStart, getDateFinish, getAllDate);
                        }
                        else
                            Toast.makeText(ChooseProductView.this, "Не коректна кінцева дата використання", Toast.LENGTH_SHORT).show();

                    }

                };

                new DatePickerDialog(ChooseProductView.this, date, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        getDateFinish.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDateFinish.callOnClick();
                }
            }
        });


        getAllDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(ChooseProductView.this);
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.button2);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                np.setMaxValue(200);
                if (getAllDate.getText().length() > 0)
                    np.setValue(Integer.parseInt(getAllDate.getText().toString()));
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        getAllDate.setText(String.valueOf(np.getValue()));
                        setCorrectDateAll(getDateStart, getDateFinish, getAllDate);
                        d.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
        getAllDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getAllDate.callOnClick();
                }
            }
        });



        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if ((getKilkist.getText().length() != 0) && (getKilkist.getText().toString().matches("\\s*\\d+(\\.?|\\,?)\\d{0,3}\\s*")))
                {
                    DataOfNakladnaPresenter.list.add(new ProductsData.Builder()
                            .setName(list.get(position).getName())
                            .setPrice(list.get(position).getPrice())
                            .setKod(list.get(position).getKod())
                            .setEduch(list.get(position).getEdYch())
                            .setUnit(list.get(position).getUnit())
                            .setKilbkistb(Double.parseDouble(getKilkist.getText().toString().replace(',','.')))
                            .setDateStart(getDateStart.getText().toString())
                            .setDateFinish(getDateFinish.getText().toString())
                            .setDateTriv(getAllDate.getText().toString())
                            .build());
                    DataOfNakladnaView.view.getAdapter().notifyDataSetChanged();
//                    DataOfNakladnaView.saveFile = false;
                    DataOfNakladnaPresenter.iDataOfNakladnaView.pressSummary(DataOfNakladnaPresenter.list);
                    ChooseProductView.super.onBackPressed();
                }
                else
                {
                    Toast.makeText(ChooseProductView.this, "Кількість введена некоректно", Toast.LENGTH_SHORT).show();
                    pressAddElement(position);
                }

            }
        });

        alert.setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private boolean checkValueDate(String min, String max)
    {
        if (max.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateMin = null;
            Date dateMax = null;
            try {
                dateMin = sdf.parse(min);
                dateMax = sdf.parse(max);
            } catch (ParseException e) {
            }
            if (dateMin != null && dateMax != null) {
                long diff = dateMax.getTime() - dateMin.getTime();
                if (diff >= 0)
                    return (true);
            }
            return (false);
        }
        return (true);
    }

    private void setCorrectDateFin(EditText edStart, EditText edFin, EditText edTriv)
    {
        String dateStart = edStart.getText().toString();
        String dateFinish = edFin.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateSt = null;
        Date dateFn = null;
        try {
            dateSt = sdf.parse(dateStart);
            dateFn = sdf.parse(dateFinish);
        }
        catch (ParseException e) {}

        if (dateSt != null && dateFn != null) {
            long diff = dateFn.getTime() - dateSt.getTime();
            edTriv.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
        }
    }

    private void setCorrectDateAll(EditText edStart, EditText edFin, EditText edTriv)
    {
        String dateStart = edStart.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateSt = null;
        try {
            dateSt = sdf.parse(dateStart);
        }
        catch (ParseException e) {}

        if (dateSt != null) {
            edFin.setText(addDay(edStart.getText().toString(), Integer.parseInt(edTriv.getText().toString())));
        }
    }

    public String addDay(String oldDate, int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_YEAR,numberOfDays);
        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date newDate=new Date(c.getTimeInMillis());
        String resultDate=dateFormat.format(newDate);
        return resultDate;
    }

    public void setMyText(TextView text)
    {
        text.setTextSize(15);
        text.setVisibility(View.VISIBLE);
    }
}
