package com.example.prodapp.Presenter.DataOfNakladna;

import com.example.prodapp.Model.Employe.Employe;
import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.View.DataOfNakladna.IDataOfNakladnaView;

import java.util.ArrayList;
import java.util.List;

public class DataOfNakladnaPresenter implements IDataOfNakladnaPresenter {

    static public IDataOfNakladnaView iDataOfNakladnaView;

    static public List<ProductsData> list;
    static public Employe employe;

//    static public SQLiteDatabase sqLiteDatabase;


    public DataOfNakladnaPresenter(IDataOfNakladnaView iDataOfNakladnaView)
    {
        this.iDataOfNakladnaView = iDataOfNakladnaView;
        list = new ArrayList<>();
        employe = new Employe();
    }

    @Override
    public void setList(List<ProductsData> list) {
        DataOfNakladnaPresenter.list = list;
    }

    @Override
    public void onSave() {
        iDataOfNakladnaView.pressSave(list);
    }

    @Override
    public void onSend() {
        iDataOfNakladnaView.pressSend();
    }

    @Override
    public void onEdit(int position) {
        iDataOfNakladnaView.pressEdit(position, list);
    }

    @Override
    public void onBack() {
        iDataOfNakladnaView.pressBack(list);
    }

    @Override
    public List<ProductsData> getList() {
        return (list);
    }


    @Override
    public void onloadEmplInfo() {
        iDataOfNakladnaView.pressLoadEmplInfo(employe);
    }

    @Override
    public Employe getEmploye() {
        return (employe);
    }

    @Override
    public void onSummary() {
        iDataOfNakladnaView.pressSummary(list);
    }

    @Override
    public IDataOfNakladnaView getIDataOfNakladnaView() {
        return iDataOfNakladnaView;
    }
}
