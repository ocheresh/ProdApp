package com.example.prodapp.Presenter.DataOfNakladna;

import com.example.prodapp.Model.Employe.Employe;
import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.View.DataOfNakladna.IDataOfNakladnaView;

import java.util.List;

public interface IDataOfNakladnaPresenter {

    void onSave();

    void onSend();

    void onEdit(int position);

    void onBack();

    List<ProductsData> getList();

    void onloadEmplInfo();

    public Employe getEmploye();

    void onSummary();

    IDataOfNakladnaView getIDataOfNakladnaView();

    public void setList(List<ProductsData> list);
}
